<template>
  <div v-loading.fullscreen.lock="loading" class="app-container">
    <el-card>
      <el-form inline>
        <el-form-item>
          <el-button icon="el-icon-search" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>
      <el-row class="mb-8">
        <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleCreate">新增</el-button>
        <!--<el-button size="mini" type="default" @click="handleRefresh">刷新</el-button>-->
      </el-row>
      <el-table :data="pageItems.rows" v-loading="tableLoading" border stripe>
        <el-table-column label="ID" prop="id"/>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="标识" prop="code"></el-table-column>
        <el-table-column label="描述" min-width="200">
          <template slot-scope="scope">
            <span class="td-word" :title="scope.row.description">{{scope.row.description}}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" fixed="right" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatus(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="140" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" size="mini" class="delete-color" v-if="scope.row.code !== 'root'"
                       @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
      <page-tag :items="pageItems" @reload="handleReload"/>

      <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" :close-on-click-modal="false">
        <el-form ref="temp" :model="temp" :rules="rules" label-width="100px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="temp.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="标识" prop="code">
            <el-input v-model="temp.code" clearable></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="temp.description" clearable type="textarea" autosize></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="temp.status" :active-value="1" :inactive-value="0"></el-switch>
          </el-form-item>
          <el-form-item label="菜单权限" v-if="!(temp.id && temp.code === 'root')">
            <el-button @click="fetchAllMenus" class="mb-8" size="mini" type="primary">刷新菜单</el-button>
            <el-tree
              ref="tree"
              v-if="dialogVisible"
              node-key="id"
              highlight-current
              :default-checked-keys="currentMenusChecked"
              :data="menus"
              :props="menusTreeProps"
              show-checkbox
              @check-change="handleMenuChange">
            </el-tree>
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
        </div>
      </el-dialog>

    </el-card>
  </div>
</template>

<script>
  export default {
    data() {
      const validateRole = (rule, value, callback) => {
        if (value != undefined) {
          this.$api.get('/roles/exists', {code: value, id: this.temp.id}).then(res => {
            if (res.data) {
              return callback(new Error('该角色标识已经存在'));
            } else {
              callback();
            }
          });
        } else {
          callback();
        }
      };
      return {
        loading: true,
        tableLoading: true,
        query: {
          page: 1,
          size: 20,
        },
        dialogVisible: false,
        dialogTitle: '新增',
        pageItems: {
          page: 1,
          size: 20,
          total: 0,
          rows: []
        },
        temp: {},
        rules: {
          name: [{required: true, trigger: 'blur', message: '请输入角色名称'}],
          code: [{required: true, trigger: 'blur', message: '请输入角色标识'}, {trigger: 'blur', validator: validateRole}]
        },
        currentMenusChecked: [],
        menusTreeProps: {
          label: 'name'
        },
        menus: []
      }
    },
    created() {
      this.fetchData()
      this.fetchAllMenus()
      this.loading = false
    },
    methods: {
      /** 获取数据 */
      fetchData() {
        this.tableLoading = true
        this.$api.get('/roles/list', this.query).then(res => {
          this.pageItems = res.data;
          this.tableLoading = false;
        })
      },

      /** 重置temp */
      resetTemp() {
        this.temp = {
          menus: []
        };
      },

      /** 查询按钮 */
      handleSearch() {
        this.query.page = 1;
        this.fetchData();
      },

      /** 分页重新加载 */
      handleReload({page, size}) {
        this.query.page = page;
        this.query.size = size;
        this.fetchData();
      },

      /** 刷新按钮 */
      handleRefresh() {
        this.fetchData();
      },

      /** 新增按钮 */
      handleCreate() {
        this.resetTemp();
        this.currentMenusChecked = [];
        this.dialogTitle = '新增';
        this.dialogVisible = true;
      },

      /** 编辑按钮 */
      handleEdit(row) {
        this.loading = true;
        this.$api.get('/roles/' + row.id, {}).then(res => {
          this.temp = res.data;
          const menus = this.temp.menus;
          this.currentMenusChecked = [];
          menus.forEach(v => {
            this.currentMenusChecked.push(v.id)
          })
          this.dialogTitle = '编辑';
          this.dialogVisible = true;
          this.loading = false;
        }, err => {
          this.loading = false;
          this.fetchData();
        });
      },

      /** 删除按钮 */
      handleDelete(row) {
        const ids = [row.id];
        this.$confirm(`确认删除为「${ids}」的角色`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(res => {
          this.$api.delete('/roles', {'ids': ids.join(',')}).then(res => {
            if (res.data && res.code === 200) {
              this.$message({type: 'success', message: '删除成功'});
              this.fetchData();
            } else {
              this.$message({type: 'error', message: '删除失败'});
            }
          })
        })
      },

      /** 更新状态 */
      handleStatus(row) {
        this.$api.post('/roles', row).then(res => {
          this.fetchData();
        })
      },

      /** 表单取消 */
      handleCancel() {
        if (this.$refs['temp']) {
          this.$refs['temp'].resetFields();
        }
        this.dialogVisible = false;
        this.resetTemp();
      },

      /** 表单提交 */
      handleSubmit() {
        this.$refs['temp'].validate(valid => {
          if (valid) {
            this.temp.menus = this.$refs['tree'].getCheckedNodes();
            this.$api.post('/roles', this.temp).then(res => {
              if (res.code === 200 && res.data) {
                this.fetchData();
                this.handleCancel();
              }
            });
          }
        });
      },

      /** 获取所有菜单 */
      fetchAllMenus() {
        this.$api.get('/menus/all', {}).then(res => {
          this.menus = res.data;
        })
      },

      /** 选择菜单 */
      handleMenuChange(data, checked, indeterminate) {
        // const nodes = this.$refs['tree'].getCheckedNodes();
        // console.log(nodes);
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
