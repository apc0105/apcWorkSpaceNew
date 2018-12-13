import request from '@/utils/request'
const prefix = '/'

export function uploadFile(formData) {
  return request.post(prefix + 'uploadFile',formData);
}
export function getAnalysisData(pageNumber,pageSize,keyword) {
  return request.post(prefix + 'getAnalysisData?pageNumber='+pageNumber+'&pageSize='+pageSize+'&keyword='+keyword);
}

