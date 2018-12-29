import request from '@/utils/request'
import qs from 'qs';
const prefix = 'apiInfo/'

export function getPackageTypes() {
  return request.post(prefix + 'getPackageTypes')
}
export function getApiInfos(packageType) {
  var params = {'packageType': packageType};
  return request.post(prefix + 'getApiInfos',qs.stringify(params))
}


