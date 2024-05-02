<template>
	<view class="trip">
		<NavBar :isBack="false" title="行程" />
		<u-tabs active-color="#FFF533" inactive-color="#FFF" bg-color="#150C24" font-size="30" :list="list"
			:is-scroll="false" :current="current" @change="change"></u-tabs>
		<!-- 我的行程 -->
		<view class="items u-flex" v-if="current===0">
			<view class="item" v-for="(item,index) in acGroupList" :key="index">
				<image src="../../static/trip-bg.png" class="trip-bg"></image>
				<view class="user">
					<image :src="item.otherAvatar">
					</image>
					<image src="../../static/link-icon-yellow.png" class="link-icon-yellow"></image>
					<image :src="item.myAvatar" mode="">
					</image>
				</view>
				<view class="title">
					{{item.title}}
				</view>
				<view class="info u-flex">
					<view class="level">
						{{item.otherLevel}}级
					</view>
					<image src="../../static/split-white.png" class="split-white"></image>
					<view class="paibi u-flex">
						<image src="../../static/paibi.png" class="paibi-icon"></image>
						X
						{{item.money}}
					</view>
				</view>
				<view class="to-ac">
					<button
						@click="toMatePage(item.activityId,item.groupId,item.applyId,item.otherUserId)">进入活动</button>
				</view>
			</view>
		</view>
		<!-- 我的发布 -->
		<view class="teamCard u-flex" v-else-if="current===1">
			<TeamCard v-for="(item,index) in acGroupListByMy" :key="index"
				@click="toUserInfoPage(item.otherUserId,item.groupId,item.activityId,'update')" class="item"
				:item="item" />
		</view>
		<!-- 申请消息 -->
		<view class="apply" v-else>
			<u-swipe-action v-for="(item,index) in applyList" :index="index" :key="index" bg-color="#150C24"
				:options="options" @click="deleteApply">
				<view class="item u-flex">
					<view class="info u-flex"
						@click="toUserInfoPage(item.otherUserId,item.groupId,item.activityId,item.applyId,'applied')">
						<image :src="item.avatar" class="avatar"></image>
						<view>
							<view class="name">{{item.nickName}}</view>
							<view class="type">申请组队“{{item.groupTitle}}”</view>
						</view>
					</view>
					<button @click="agreenApply(item)">同意</button>
				</view>
				<view class="msg">
					留言：{{item.message}}
				</view>
				<image src="../../static/split-line.png" class="split-line" v-if="index !==applyList.length-1"></image>
			</u-swipe-action>
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import TeamCard from '@/components/teamCard/index.vue'
	import Api from "@/api/index.js"
	import {
		mapState
	} from 'vuex'

	export default {
		components: {
			NavBar,
			TeamCard
		},
		computed: {
			...mapState(['userInfo'])
		},
		watch: {
			current(val) {
				if (val == 0) {
					this.getMyTripsList()
				} else if (val == 1) {
					this.getActivityGroupListByMy()
				}else{
					this.getApplyList()
				}
			}
		},
		data() {
			return {
				options: [{
					text: '删除',
					style: {
						backgroundColor: '#dd524d'
					}
				}],
				list: [{
					name: '我的行程'
				}, {
					name: '我的发布'
				}, {
					name: '申请消息'
				}],
				current: 0,
				acGroupList: [],
				acGroupListByMy: [],
				applyList: []
			}
		},
		methods: {
			deleteApply(index) {
				let _this = this;
				const {
					applyId
				} = this.applyList[index]
				Api.rejectApply(applyId, (res) => {
					_this.getApplyList()
				})
			},
			change(index) {
				this.current = index;
			},
			// 前往活动信息修改页面
			toAcInfoUpdatePage(id) {
				uni.navigateTo({
					url: "/pages/party/update/index?activityId=" + id
				})
			},
			// 前往个人信息页面
			toUserInfoPage(userId, groupId, activityId, applyId, type) {
				uni.navigateTo({
					url: `/pages/info/index?groupId=${groupId}&type=${type}&activityId=${activityId}&userId=${userId}&applyId=${applyId}`
				})
			},
			// 前往匹配页面
			toMatePage(activityId, groupId, applyId, userId) {
				uni.navigateTo({
					url: `/pages/trip/mate/index?activityId=${activityId}&groupId=${groupId}&applyId=${applyId}&userId=${userId}`
				})
			},
			// 我的发布列表
			getActivityGroupListByMy() {
				Api.getActivityGroupList({
					userId: this.userInfo.userId
				}, (res) => {
					const data = res.data.rows
					this.acGroupListByMy = data
				})
			},
			// 获取申请列表
			getApplyList() {
				Api.getApplyList((res) => {
					const data = res.data.data
					this.applyList = data
				})
			},
			// 同意组队
			agreenApply(item) {
				Api.agreeApply(item.applyId, () => {
					this.getApplyList()
				})
			},
			// 我的行程
			getMyTripsList() {
				Api.getMyTripsList((res) => {
					const data = res.data.data
					this.acGroupList = data
				})
			},
		},
		onLoad() {
			this.getActivityGroupListByMy()
			this.getApplyList()
			this.getMyTripsList()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>