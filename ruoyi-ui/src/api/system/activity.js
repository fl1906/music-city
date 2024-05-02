import request from '@/utils/request'

// 查询活动列表
export function listActivity(query) {
  return request({
    url: '/system/activity/list',
    method: 'get',
    params: query
  })
}

// 查询活动详细
export function getActivity(activityId) {
  return request({
    url: '/system/activity/' + activityId,
    method: 'get'
  })
}

// 新增活动
export function addActivity(data) {
  return request({
    url: '/system/activity',
    method: 'post',
    data: data
  })
}

// 修改活动
export function updateActivity(data) {
  return request({
    url: '/system/activity',
    method: 'put',
    data: data
  })
}

// 删除活动
export function delActivity(activityId) {
  return request({
    url: '/system/activity/' + activityId,
    method: 'delete'
  })
}

// 获取地址列表
export function getAddressList() {
  return request({
    url: '/system/region/list',
    method: 'get'
  })
}
