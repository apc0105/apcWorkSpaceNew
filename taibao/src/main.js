// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
//解决Ie8以上兼容问题
import 'babel-polyfill'
import 'url-search-params-polyfill'
import 'es6-promise/auto'
import promise from 'es6-promise'
promise.polyfill();

import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import '@/styles/global.scss'
import 'element-ui/lib/theme-chalk/index.css';
import 'nprogress/nprogress.css'
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
