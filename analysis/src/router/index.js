import Vue from 'vue'
import Router from 'vue-router'
import homePage from '@/view/page/homePage'

Vue.use(Router)

const router = new Router({
  mode:"history",
  base:"/analysis/",
  routes: [
    {
      path: '/',
      name: 'homePage',
      component: homePage
    }
  ]
})

export default router
