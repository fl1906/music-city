import request from '@/utils/request'

// 查询活动组队列表
export function listActivityGroup(query) {
  return request({
    url: '/system/activityGroup/list',
    method: 'get',
    params: query
  })
}

// 查询活动组队详细
export function getActivityGroup(groupId) {
  return request({
    url: '/system/activityGroup/' + groupId,
    method: 'get'
  })
}

// 新增活动组队
export function addActivityGroup(data) {
  return request({
    url: '/system/activityGroup',
    method: 'post',
    data: data
  })
}

// 修改活动组队
export function updateActivityGroup(data) {
  return request({
    url: '/system/activityGroup',
    method: 'put',
    data: data
  })
}

// 删除活动组队
export function delActivityGroup(groupId) {
  return request({
    url: '/system/activityGroup/' + groupId,
    method: 'delete'
  })
}
