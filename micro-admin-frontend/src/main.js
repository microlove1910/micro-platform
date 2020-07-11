import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Cookies from 'js-cookie'
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
import PageTag from '@/components/PageTag'
import Api from '@/api/api'
import * as filters from './filters'; // global filters

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key]);
});

Vue.component('page-tag', PageTag)

Vue.prototype.$api = new Api()

Vue.use(ElementUI, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
