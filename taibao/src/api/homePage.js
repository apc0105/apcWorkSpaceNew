import request from '@/utils/request'
const prefix = 'web/PageHome/'
import store from '@/store'

export function getRecommend(filter) {
  return request.post(prefix + 'getRecommend/?token='+store.getters.token, filter)
}
export function getHistoricalRecords(filter) {
  return request.post(prefix + 'getHistoricalRecords/?token='+store.getters.token, filter)
}
export function closeSerMess(filter) {
  return request.post(prefix + 'closeSerMess/?token='+store.getters.token, filter)
}


