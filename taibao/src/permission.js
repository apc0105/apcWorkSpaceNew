import router from './router'
import store from '@/store'
const whiteList = ['/login'];

router.beforeEach((to, from, next) => {
  let token=store.getters.token;
  if (token){
    next()
    //todo 此处还需要校验token是否失效
  }else {
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else {
      next('/login') // 否则全部重定向到登录页
    }
  }
})

