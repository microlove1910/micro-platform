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

      </el-table>
      <page-tag :items="pageItems" @reload="handleReload" />

      <el-dialog :visible.sync="dialogVisible" :title="dialogTitle" :close-on-click-modal="false">
        <el-form ref="temp" v-model="temp" :rules="rules">

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
        rules: {}
      }
    },
    created() {
      this.fetchData()
      this.loading = false
    },
    methods: {
      fetchData() {
        this.tableLoading = false
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

      /** 编辑按钮 */
      handleEdit(row) {
        this.temp = Object.assign({}, row);
        this.dialogTitle = '编辑';
        this.dialogVisible = true;
      },

      /** 表单取消 */
      handleCancel() {
        if (this.$refs['temp']) {
          this.$refs['temp'].resetField();
        }
        this.dialogVisible = false;
        this.resetTemp();
      },

      /** 表单提交 */
      handleSubmit() {
        this.$refs['temp'].validate(valid => {
          if (valid) {

            this.handleCancel();
          }
        });
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
