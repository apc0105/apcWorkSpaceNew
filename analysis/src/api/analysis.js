import request from '@/utils/request'
import qs from 'qs';

const prefix = '/'

export function uploadFile(formData) {
  return request.post(prefix + 'uploadFile', formData);
}

export function getAnalysisData(pageNumber, pageSize, keyword) {
  var params = {'pageNumber': pageNumber, 'pageSize': pageSize, 'keyword': keyword};
  return request.post(prefix + 'getAnalysisData', qs.stringify(params));
}

export function getAnalysisDetail(UID,index,newstarget,pageNumber, pageSize) {
  var params = {'pageNumber': pageNumber, 'pageSize': pageSize, 'index': index,'UID':UID ,"newstarget": newstarget};
  return request.post(prefix + 'getAnalysisDetail', qs.stringify(params));
}

