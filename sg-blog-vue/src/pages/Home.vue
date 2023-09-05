<!-- 首页 -->
<template>
    <div>
        <sg-navbar></sg-navbar>
        <div class="container">
            <el-row  :gutter="30"  class="father">
                    <el-col  class="float1" :sm="24" :md="16" style="transition:all .5s ease-out;margin-bottom:30px;">
                    <sg-articlelist></sg-articlelist>
                </el-col>
                    <el-col class="float" :class="isVisiable?'sticky':''"  :sm="24"  :md="8" >
                    <sg-rightlist ></sg-rightlist>
                </el-col>
               
            </el-row>
        </div>
        <!-- <div class="container">
        <div class="father">
            <div  class="float1" style="transition:all .5s ease-out;margin-bottom:30px;">
                <sg-articlelist></sg-articlelist>
            </div>
            <div class="float" :class="isVisiable?'sticky':''">
                <sg-rightlist ></sg-rightlist>
            </div>
        </div> -->
    <!-- </div>  -->
    </div>
</template>

<script>
import header from '../components/header.vue'
import articlelist from '../components/articlelist.vue'
import rightlist from '../components/rightlist.vue'

    export default {
        name:'Home',
        data() { //选项 / 数据
            return {
              isVisiable:false

            }
        },
        methods: { //事件处理器
           //监听窗口滚动
            windowScrollListener() {
            //获取操作元素最顶端到页面顶端的垂直距离
            var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
           
            var  clientWidth=document.body.clientWidth;
            if (scrollTop >= 980&&clientWidth>=992) {
                this.isVisiable = true; //大于480时显示元素
            }
            if (scrollTop < 980) {
                this.isVisiable = false; //小于480时隐藏元素
            }
            }
        },
        components: { //定义组件
            'sg-navbar':header,
            'sg-articlelist':articlelist,
            'sg-rightlist':rightlist
        },
        beforedestroyed() {
   //离开页面时删除该监听
   window.removeEventListener('scroll', this.windowScrollListener)
        },
        created() { //生命周期函数
            window.addEventListener('scroll', this.windowScrollListener);
            this.floatElement();
            
        },
        mounted(){
            window.onresize = () => {
                var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
           
           var  clientWidth=document.body.clientWidth;

        return (() => {
            if (clientWidth < 992) {
                this.isVisiable = false; //小于480时隐藏元素
            }
        })();
      };
        }
    
    }
</script>

<style scoped>

.sticky {
  position: fixed;
  bottom: 0;
  right: 9%;
  width: 27%;
  }
</style>
