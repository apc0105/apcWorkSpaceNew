import {loginByUsername, logout, getUserInfo} from '@/api/login'
import {getSingleUrlParam} from '@/utils/RequestLocationUrl'

const user = {
  state: {
    user: '',
    token: getSingleUrlParam("token"),
  },

  mutations: {

    SET_TOKEN: (state, token) => {
      state.token = token
    },
  },

  actions: {
    // 用户名登录
    LoginByUsername({commit}, userInfo) {
      return new Promise((resolve, reject) => {
        loginByUsername(userInfo).then(response => {
          const token = response.data
          commit('SET_TOKEN', token)

          resolve(token)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({commit, state}) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')

          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')

        resolve()
      })
    },
  }
}

export default user
