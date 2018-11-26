import request from '@/utils/request'
const prefix = 'apiInfo/'

export function getPackageTypes() {
  return request.post(prefix + 'getPackageTypes')
}
export function getApiInfos(packageType) {
  return request.post(prefix + 'getApiInfos?packageType='+packageType)
}


