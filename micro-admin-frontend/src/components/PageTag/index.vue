<template>
  <div style="display: flex;justify-content: flex-end;margin-top: 15px;">
    <el-pagination
      :current-page="items.page"
      :page-sizes="[10, 15, 20, 50, 100]"
      :page-size="items.size"
      :total="items.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
  export default {
    name: 'PageTag',
    props: {
      items: {
        type: Object,
        required: true
      }
    },
    methods: {
      handleSizeChange(val) {
        var page = 1;
        var size = val;
        this.$emit('reload', {size, page});
      },
      handleCurrentChange(val) {
        var size = this.items.pageSize;
        var page = val;
        this.$emit('reload', {size, page});
      }
    }
  };
</script>
