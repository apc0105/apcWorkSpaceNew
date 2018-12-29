// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
//解决Ie8以上兼容问题
import 'babel-polyfill'
import 'url-search-params-polyfill'
import 'es6-promise/auto'
import promise from 'es6-promise'
promise.polyfill();

import {Promise} from 'es6-promise-polyfill';

import Vue from 'vue';
import App from './App';
import router from './router';

import ElementUI from 'element-ui';
import axios from 'axios'

import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false

Vue.prototype.baseUrl=process.env.BASE_API;
Vue.prototype.$axios = axios

Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
