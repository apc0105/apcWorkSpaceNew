import request from '@/utils/request'
import qs from 'qs';

const prefix = 'TKQuantization/'

export function getQuantPage(pageSize, days, pageNumber, fPageNumber, floatNum, subfield) {
  var params = {
    'pageSize': pageSize,
    'days': days,
    'pageNumber': pageNumber,
    'fPageNumber': fPageNumber,
    'floatNum': floatNum,
    "subfield": subfield
  };
  return request.post(prefix + 'getQuantPage', qs.stringify(params))
}


