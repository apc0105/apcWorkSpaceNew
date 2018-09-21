// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import '@/styles/global.scss'
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import global_ from './components/Global'

import {forwardWindowByAction, getSingleUrlParam,windowHrefByAction} from '@/utils/RequestLocationUrl'

Vue.config.productionTip = false

Vue.prototype.baseUrl=process.env.BASE_API;
Vue.prototype.$axios = axios
//Vue.config.productionTip = false
Vue.use(ElementUI)
/* eslint-disable no-new */

Vue.prototype.GLOBAL = global_
axios.defaults.withCredentials=true

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})