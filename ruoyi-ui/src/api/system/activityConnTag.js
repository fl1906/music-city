import request from '@/utils/request'

// 查询活动标签与活动关联列表
export function listActivityConnTag(query) {
  return request({
    url: '/system/activityConnTag/list',
    method: 'get',
    params: query
  })
}

// 查询活动标签与活动关联详细
export function getActivityConnTag(activityConnTagId) {
  return request({
    url: '/system/activityConnTag/' + activityConnTagId,
    method: 'get'
  })
}

// 新增活动标签与活动关联
export function addActivityConnTag(data) {
  return request({
    url: '/system/activityConnTag',
    method: 'post',
    data: data
  })
}

// 修改活动标签与活动关联
export function updateActivityConnTag(data) {
  return request({
    url: '/system/activityConnTag',
    method: 'put',
    data: data
  })
}

// 删除活动标签与活动关联
export function delActivityConnTag(activityConnTagId) {
  return request({
    url: '/system/activityConnTag/' + activityConnTagId,
    method: 'delete'
  })
}
