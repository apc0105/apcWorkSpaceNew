import request from '@/utils/request'
import qs from 'qs';

const prefix = 'TKNewsServlet/'

export function smartSearch(pageSize, nDirection, searchValue, flPriceChng) {
  var params = {'pageSize': pageSize, 'nDirection': nDirection,'search_value':searchValue,'flPriceChng':flPriceChng};
  return request.post(prefix + 'SmartSearch',qs.stringify(params))
}

export function smartSearchPage(pageSize, nDirection, pageNumber, fPageNumber, token, flPriceChng) {
  var params = {'pageSize': pageSize, 'nDirection': nDirection,'pageNumber':pageNumber,'fPageNumber':fPageNumber,'token':token,'flPriceChng':flPriceChng};
  return request.post(prefix + 'SmartSearchPage',qs.stringify(params))
}

export function smartSearchEdge(code, token) {
  var params = {'code': code, 'token': token};
  return request.post(prefix + 'SmartSearchEdge', qs.stringify(params))
}

export function searchHintKeys(keyWords) {
  var params = {'keyWords': keyWords};
  return request.post(prefix + 'searchHintKeys', qs.stringify(params))
}

