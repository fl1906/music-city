import request from '@/utils/request'

// 查询订单列表
export function listPzc_order(query) {
  return request({
    url: '/system/pzc_order/list',
    method: 'get',
    params: query
  })
}

// 查询订单详细
export function getPzc_order(orderId) {
  return request({
    url: '/system/pzc_order/' + orderId,
    method: 'get'
  })
}

// 新增订单
export function addPzc_order(data) {
  return request({
    url: '/system/pzc_order',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updatePzc_order(data) {
  return request({
    url: '/system/pzc_order',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delPzc_order(orderId) {
  return request({
    url: '/system/pzc_order/' + orderId,
    method: 'delete'
  })
}
