import request from '@/utils/request'
const prefix = 'TKNewsServlet/'

export function smartSearch(nDirection,searchValue,flPriceChng) {
  return request.post(prefix + 'SmartSearch?nDirection='+nDirection+'&search_value='+searchValue+'&flPriceChng='+flPriceChng)
}
export function smartSearchPage(nDirection,pageNumber,token,flPriceChng) {
  return request.post(prefix + 'SmartSearchPage?nDirection='+nDirection+'&pageNumber='+pageNumber+'&token='+token+'&flPriceChng='+flPriceChng)
}
export function smartSearchEdge(code,token) {
  return request.post(prefix + 'SmartSearchEdge?code='+code+'&token='+token)
}
export function searchHintKeys(keyWords){
  return request.post(prefix + 'searchHintKeys?keyWords='+keyWords)
}

