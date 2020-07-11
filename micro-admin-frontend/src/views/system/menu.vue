<template>
  <div v-loading.fullscreen.lock="loading" class="app-container">
    <el-card>
      <!--<el-form inline>-->
      <!--  <el-form-item>-->
      <!--    <el-button icon="el-icon-search" @click="handleSearch">查询</el-button>-->
      <!--  </el-form-item>-->
      <!--</el-form>-->
      <el-row class="mb-8">
        <el-button size="mini" type="primary" @click="handleCreate">新增</el-button>
        <el-button size="mini" type="default" @click="handleRefresh">刷新</el-button>
      </el-row>
      <el-table :data="pageItems.rows" v-loading="tableLoading" border stripe row-key="id"
                :tree-props="{children: 'children'}">
        <el-table-column prop="id" label="ID"></el-table-column>
        <el-table-column prop="name" label="菜单名称"></el-table-column>
        <el-table-column prop="path" label="路径"></el-table-column>
        <el-table-column label="状态" width="80px" align="center">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" :inactive-value="0" :active-value="1"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" class="delete-color"
                       @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--<page-tag :items="pageItems" @reload="handleReload"/>-->

      <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" :close-on-click-modal="false">
        <el-form ref="temp" :model="temp" :rules="rules" label-width="100px">
          <el-form-item label="上级菜单" prop="parentId">
            <el-select v-model="temp.parentId" placeholder="请选择">
              <template v-for="item in options">
                <el-option v-if="item.value !== temp.id"
                           :key="item.value"
                           :label="item.label"
                           :value="item.value">
                </el-option>
              </template>
            </el-select>
          </el-form-item>
          <el-form-item label="菜单名称" prop="name">
            <el-input v-model="temp.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="菜单路径" prop="path">
            <el-input v-model="temp.path"></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="temp.status" :inactive-value="0" :active-value="1"></el-switch>
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
          parentId: [{required: true, trigger: 'change', message: '请选择上级菜单'}],
          name: [{required: true, trigger: 'blur', message: '请输入菜单名称'}],
          path: [{required: true, trigger: 'blur', message: '请输入菜单路径'}]
        },
        options: [],
      }
    },
    created() {
      this.fetchData()
      this.loading = false
    },
    methods: {
      fetchData() {
        this.tableLoading = true
        this.$api.get('/menus/all', {}).then(res => {
          this.pageItems.rows = res.data;
          this.options = [{label: '主目录', value: 0}];

          for (const r of this.pageItems.rows) {
            this.options.push({label: r.name, value: r.id})
          }

          this.tableLoading = false;
        });
      },

      /** 重置temp */
      resetTemp() {
        this.temp = {};
      },

      // 查询按钮
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
        this.dialogTitle = '新增';
        this.dialogVisible = true;
        this.resetTemp();
      },

      /** 删除按钮 */
      handleDelete(row) {
        this.$confirm(`确认删除为「${row.name}」的菜单`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(res => {
          this.$api.delete('/menus', {'id': row.id}).then(res => {
            if (res.data && res.code === 200) {
              this.$message({type: 'success', message: '删除成功'});
              this.fetchData();
            } else {
              this.$message({type: 'error', message: '删除失败'});
            }
          })
        })
      },

      /** 编辑按钮 */
      handleEdit(row) {
        this.temp = Object.assign({}, row);
        this.dialogTitle = '编辑';
        this.dialogVisible = true;
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
            console.log(this.temp);
            this.$api.post('/menus', this.temp).then(res => {
              this.fetchData();
              this.handleCancel();
            });
          }
        });
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
