// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import './assets/css/style.less'
import store from './store'
import MavonEditor from 'mavon-editor'
import floatElement from './assets/js/floatElement'
import VueLazyload from "vue-lazyload";

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(MavonEditor)
Vue.prototype.floatElement = floatElement

//或者自定义配置插件
Vue.use(VueLazyload, {
preLoad: 1.3,
error: require('../src/assets/img/懒加载.gif'),
loading: require("../src/assets/img/懒加载.gif"),
attempt: 5
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  store
})
