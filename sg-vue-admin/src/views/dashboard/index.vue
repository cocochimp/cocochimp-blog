<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      cocochimp个人博客后台系统
    </div>
    <div>
      本项目前后花费了1 week的时间进行开发
    </div>
    <div>
      非常感谢三更老师的博客教程！
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
// .dashboard-container{
//   min-height: 100%;
//   width: 100%;
//   background-image: url("https://cocochimp-markdown-img.oss-cn-beijing.aliyuncs.com/save/background.jpg");
//   background-size:cover;  /* 让背景图基于容器大小伸缩 */
//   background-attachment:fixed;  /* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */    //此条属性必须设置否则可能无效/
//   background-color: #2d3a4b;  /* 设置背景颜色，背景图加载过程中会显示背景色 */
//   overflow: hidden;
// }

.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
