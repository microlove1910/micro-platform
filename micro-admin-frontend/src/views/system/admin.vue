<template>
  <div v-loading.fullscreen.lock="loading" class="app-container">
    <el-card>
      <el-form inline>
        <el-form-item>
          <el-button icon="el-icon-search" @click="handleSearch">查询</el-button>
        </el-form-item>
      </el-form>
      <el-row class="mb-8">
        <el-button size="mini" type="primary" @click="handleCreate">新增</el-button>
        <el-button size="mini" type="default" @click="handleRefresh">刷新</el-button>
      </el-row>
      <el-table :data="pageItems.rows" v-loading="tableLoading" border stripe>
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatus(scope.row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="160" align="center">
          <template slot-scope="scope">
            <span>{{scope.row.createAt | parseTime}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" class="delete-color"
                       @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <page-tag :items="pageItems" @reload="handleReload"/>

      <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" :close-on-click-modal="false">
        <el-form ref="temp" :model="temp" :rules="rules" label-width="100px">
          <el-form-item label="账号" prop="username">
            <el-input v-model="temp.username" clearable></el-input>
          </el-form-item>
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="temp.nickname" clearable></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="temp.password" clearable type="password"></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="temp.status"
              :active-value="1"
              :inactive-value="0"
            ></el-switch>
          </el-form-item>
          <el-form-item label="角色">
            <el-button size="mini" type="primary" icon="el-icon-refresh" @click="fetchAllRoles" class="mb-8">刷新角色
            </el-button>
            <el-tree
              ref="tree"
              v-if="dialogVisible"
              node-key="id"
              highlight-current
              :default-checked-keys="currentTreeCheckedKeys"
              :data="roles"
              :props="{label: 'name'}"
              show-checkbox>
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
      const validateUsername = (rule, value, callback) => {
        if (value == undefined || value.length < 4) {
          return callback(new Error('请输入大于4位的账号'));
        }
        this.$api.get('/admins/exists', {username: value, id: this.temp.id}).then(res => {
          if (res.data) {
            return callback(new Error('该账号已经被使用，请更换'))
          }
          return callback();
        });
      };
      const validatePassword = (rule, value, callback) => {
        if (value != undefined && value.length < 5 && value.length > 0) {
          return callback(new Error('密码长度必须大于5位字符'))
        } else {
          return callback()
        }
      }
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
          username: [
            {required: true, trigger: 'blur', message: '请输入账号'},
            {trigger: 'blur', validator: validateUsername}
          ],
          nickname: [{required: true, trigger: 'blur', message: '请输入昵称', min: 4}],
          password: [{trigger: 'blur', validator: validatePassword}]
        },
        roles: [],
        currentTreeCheckedKeys: [],
      }
    },
    created() {
      this.fetchData()
      this.fetchAllRoles()
      this.loading = false
    },
    methods: {
      fetchData() {
        this.tableLoading = true
        this.$api.get('/admins/list', this.query).then(res => {
          this.pageItems = res.data;
          this.tableLoading = false;
        })
      },

      fetchAllRoles() {
        this.$api.get('/roles/all', {}).then(res => {
          this.roles = res.data;
        })
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

      },

      /** 新增按钮 */
      handleCreate() {
        this.dialogTitle = '新增';
        this.dialogVisible = true;
        this.resetTemp();
      },

      /** 更新状态 */
      handleStatus(row) {
        this.$api.post('/admins', row).then(res => {
          this.fetchData();
        })
      },

      /** 编辑按钮 */
      handleEdit(row) {
        this.$api.get('/admins/' + row.id, {}).then(res => {
          this.currentTreeCheckedKeys = [];
          this.temp = res.data;
          this.dialogTitle = '编辑';
          this.dialogVisible = true;

          for (const role of this.temp.roles) {
            this.currentTreeCheckedKeys.push(role.id);
          }
        })
      },

      handleDelete(row) {
        const ids = [row.id];
        this.$confirm(`确认删除为「${ids}」的账号`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(res => {
          this.$api.delete('/admins', {'ids': ids.join(',')}).then(res => {
            if (res.data && res.code === 200) {
              this.$message({type: 'success', message: '删除成功'});
              this.fetchData();
            } else {
              this.$message({type: 'error', message: '删除失败'});
            }
          })
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
            this.temp.roles = this.$refs['tree'].getCheckedNodes();
            this.$api.post('/admins', this.temp).then(res => {
              this.fetchData();
              this.handleCancel();
            })
          }
        });
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
