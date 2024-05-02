import {
	Get,
	Post,
	Delete,
	UploadImage,
	Put
} from "../utils/http.js"

const wxLogin = (data, success) => {
	Post("/wx/user/login", data, success)
}

// 获取个人信息
const getUserInfo = (success) => {
	Get('/wx/user/userInfo', {
		time: new Date().getTime()
	}, success)
}

// 更新个人信息
// parmas Object
const putUserInfo = (data, success) => {
	Post('/wx/user/updateUserInfo', data, success)
}

// 充值
const getPayParams = (data, success) => {
	Post("/wx/user/recharge", data, success)
}

// 好友消息列表
const getTalkMylist = (success) => {
	Get('/system/userTalk/myList', {}, success)
}

// 单聊消息列表
// parmas toUserId
const getUserTalk = (data, success) => {
	Get('/system/userTalk/list', data, success)
}

// 操作消息已读
// parmas userId
const PostTalkRead = (data, success) => {
	Post(`/system/userTalk/read?userId=${data}`, {}, success)
}

// 查询用户在线状态
// parmas userId
const getTalkUserLine = (data, success) => {
	Get('/system/userTalk/live', data, success)
}

// 新增活动
// parmas Object
const postActivity = (data, success) => {
	Post('/system/activity', data, success)
}

// 修改活动组队信息
const postTeamUpInfo = (data, success) => {
	Put('/system/activityGroup', data, success)
}

// 删除活动
const deleteActivity = (data) => {
	Delete('/system/activity/' + data)
}

// 活动列表
// parmas 
// pageNum pageSize classify 0 是电音节 1是派对 region 0 是国际 1是国内 针对电音节有效 地区 regionId title 是搜索
const getListActivity = (data, success) => {
	Get('/system/activity/listWx', data, success)
}

// 收藏活动
// parmas activityId
const postNavActivity = (data, success) => {
	Post('/system/userCollect', data, success)
}

// 查询收藏活动状态 type 0 电音节 1 派对
const getNavActivityAndList = (data, success) => {
	Get('/system/userCollect/list', data, success)
}

// 活动详情
const getActivityDetail = (data, success) => {
	Get(`/system/activity/${data}`, {}, success)
}

// 获取学校列表
// parmas schoolName
const getSchoolList = (data, success) => {
	Get('/wx/user/getSchoolList', data, success)
}

// 上传图片
const postImageFile = (success) => {
	uni.chooseImage({
		success(res) {
			const path = res.tempFilePaths
			UploadImage(path[0], success)
		}
	})
}

// 将地址上传到相册
const postImageUrlsToPhoto = (file, success) => {
	Post('/system/userPhoto', {
		url: file
	}, success)
}

// 个人相册列表
const getUserImageList = (data, success) => {
	Get('/system/userPhoto/list', data, success)
}

// 删除相册
const deleteUserImage = (data, success) => {
	Delete('/system/userPhoto/1', data, success)
}

// 添加组队
const postActivityGroup = (data, success) => {
	Post('/system/activityGroup', data, success)
}

// 修改组队
const putActivityGroup = (data, success) => {
	Put('/system/activityGroup', data, success)
}

// 组队活动列表
const getActivityGroupList = (data, success) => {
	Get('/system/activityGroup/list', data, success)
}

// 我申请的组队列表
const getActivityGroupApplyList = (success) => {
	Get('/system/activityGroupApply/list', {}, success)
}
// 组队详情
const getActivityGroupDetail = (data, success) => {
	Get(`/system/activityGroup/${data}`, {}, success)
}

// 根据字典类型和搜索值获取字典分页列表
const getDictList = (data, success) => {
	Get('/system/dict/data/list', data, success)
}

// 首页轮播图
const getViewPageList = (success) => {
	Get('/system/viewPager/list', {}, success)
}

// 申请组队列表
const getApplyList = (success) => {
	Get('/system/activityGroup/applyList', {}, success)
}

// 申请组队
const postTeamApply = (data, success) => {
	Post('/system/activityGroupApply', data, success)
}

// 同意组队
const agreeApply = (data, success) => {
	Post('/system/activityGroup/apply?applyId=' + data, {}, success)
}

// 拒绝申请
const rejectApply = (data, success) => {
	Delete(`/system/activityGroupApply/${data}`, {}, success)
}

// 地区列表
const getRegionList = (success) => {
	Get('/system/region/list', {}, success)
}

// 查看组队个人信息
// params userId groupId
const getTeamUpUserInfo = (data, success) => {
	Get('/system/activityGroup/userInfo', data, success)
}

// 取消组队
// params applyId
const cancelTeamUp = (data, success) => {
	Post(`/system/activityGroup/cancel?applyId=${data}`, {}, success)
}

// 组队确认
// params applyId
const confirmTeamUp = (data, success) => {
	Post(`/system/activityGroup/confirm?applyId=${data}`, {}, success)
}

// 我的行程
const getMyTripsList = (success) => {
	Get('/system/activityGroupApply/myTrips', {}, success)
}

// 判断申请方还是发起方
// parmas applyId
const getApplyRole = (data, success) => {
	Get(`/system/activityGroup/applyRole?applyId=${data}`, {}, success)
}

// 根据applyId查询组队信息
// parmas applyId
const getTeamUpInfoByApplyId = (data, success) => {
	Get(`/system/activityGroupApply/${data}`, {}, success)
}

// 同意用户申请时 查看申请人的个人信息 
// parmas userId groupId
const getApplyUserInfo = ({
	userId,
	groupId
}, success) => {
	Get(`/system/activityGroup/userInfo?userId=${userId}&groupId=${groupId}`, {}, success)
}

// 超限制确认到达
// parmas applyId wxz 0 拒绝 1 同意
const postWxzApply = ({
	applyId,
	wxz
}, success) => {
	Post(`/system/activityGroupApply/wxzApply?applyId=${applyId}&wxz=${wxz}`, {}, success)
}

// 刷新地址
const postRefurbish = (data, success) => {
	Post("/system/activityGroup/refurbish", data, success)
}

// 确认到达目的地
const confirmReach = (data, success) => {
	Post(`/system/activityGroup/confirmReach?applyId=${data}`, {}, success)
}

// 获取未读消息红点状态
const getNotRead = (success) => {
	Get("/wx/user/notRead", {}, success)
}

// 获取音频
const getMusic = (success) => {
	Get("/wx/user/music", {}, success)
}

// 获取账户列表信息 
// params Object type: [1,2,3,4] 1、浏览活动 2、充值派币 3、收藏活动4、消费派币 nowTime 时间筛选
const getHisotyPayList = (data, success) => {
	Get(`/system/userHistory/list`, data, success)
}
export default {
	wxLogin,
	getPayParams,
	getTalkMylist,
	getUserInfo,
	getUserTalk,
	getTalkUserLine,
	PostTalkRead,
	getListActivity,
	postImageFile,
	postImageUrlsToPhoto,
	getUserImageList,
	putUserInfo,
	getSchoolList,
	getDictList,
	getViewPageList,
	getActivityDetail,
	postNavActivity,
	getActivityGroupList,
	getApplyList,
	agreeApply,
	getNavActivityAndList,
	getRegionList,
	postActivityGroup,
	postTeamApply,
	rejectApply,
	getTeamUpUserInfo,
	getActivityGroupDetail,
	postTeamUpInfo,
	cancelTeamUp,
	getActivityGroupApplyList,
	getMyTripsList,
	getApplyRole,
	getTeamUpInfoByApplyId,
	getApplyUserInfo,
	putActivityGroup,
	postWxzApply,
	postRefurbish,
	confirmTeamUp,
	confirmReach,
	getNotRead,
	getMusic,
	getHisotyPayList
}