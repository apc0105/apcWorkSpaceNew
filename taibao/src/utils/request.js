import promise from 'es6-promise'
promise.polyfill();

import axios from 'axios'
import {Message} from 'element-ui'
import router from '@/router'

// create an axios instance
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 20000, // request timeout
  retry: 4,
  retryDelay: 1000,
  transformRequest: [function (data) {
    // Do whatever you want to transform the data
    return data
  }],
})
// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent
  // if (typeof(store.getters.token) != 'undefined') {
  //   // 让每个请求携带token-- ['auth-token']为自定义key 请根据实际情况自行修改
  //   config.headers['Authorization'] = store.getters.token;
  // }
  return config
}, error => {
  // Do something with request error
  console.warn(error) // for debug
  promise.reject(error)
})

// respone interceptor
service.interceptors.response.use(
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过xmlhttprequest来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  response => {
    const res = response
    if (res.status !== 200) {
      Message({
        message: res.msg,
        type: 'error',
        duration: 5 * 1000
      })
      return promise.reject('error')
    } else {
      return res
    }
  },
  error => {
    if (!error.response) {
      Message.error("无法连接服务器");
    } else if (error.response.status === 401) {
      router.push('/login')
      Message.error(error.response.data.msg)
    } else if (error.response.status === 403) {
      router.push('/403')
      Message.error(error.response.data.msg)
    } else {
      Message.error('系统异常')
    }
    return promise.reject(error)
  })
//增加重试机制
// service.interceptors.response.use(undefined, function axiosRetryInterceptor(err) {
//   var config = err.config;
//   // If config does not exist or the retry option is not set, reject
//   if(!config || !config.retry) return Promise.reject(err);
//
//   // Set the variable for keeping track of the retry count
//   config.__retryCount = config.__retryCount || 0;
//
//   // Check if we've maxed out the total number of retries
//   if(config.__retryCount >= config.retry) {
//     // Reject with the error
//     return Promise.reject(err);
//   }
//
//   // Increase the retry count
//   config.__retryCount += 1;
//
//   // Create new promise to handle exponential backoff
//   var backoff = new Promise(function(resolve) {
//     setTimeout(function() {
//       resolve();
//     }, config.retryDelay || 1);
//   });
//
//   // Return the promise in which recalls axios to retry the request
//   return backoff.then(function() {
//     return axios(config);
//   });
// });
export default service
