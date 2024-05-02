<template>
	<view class="info">
		<NavBar :isBack="false" title="信息" />
		<image :src="currentImage" class="bg" mode="aspectFill"></image>
		<view class="tuku u-flex">
			<view class="item">
				<image :src="myUserInfo.avatar" :class="{active:currentImage===myUserInfo.avatar}"
					@click="setCurrentImage(myUserInfo.avatar)" mode="aspectFill">
				</image>
			</view>
			<view class="item" v-for="(item,index) in group.photo" :key="index" @click="setCurrentImage(item.url)">
				<image :src="item.url" :class="{active:currentImage===item.url}" mode="aspectFill"></image>
			</view>
		</view>
		<view class="content">
			<view class="title" @click="toActivityPage(group.activityId)">
				准备参加“<text>{{group.activityTitle}}</text>”
			</view>
			<view class="ac-info u-flex">
				<view class="item u-flex">
					<view class="name">活动主题</view>
					<view>{{group.title}}</view>
				</view>
				<view class="item u-flex">
					<view class="name">活动保障金</view>
					<view class="u-flex fee">
						<image src="../../static/paibi.png" class="paibi"></image>
						<view class="tag">X</view>
						<view class="count">{{group.money}}</view>
					</view>
				</view>
				<view class="item u-flex">
					<view class="name">活动经费</view>
					<view>{{returnGroupTypeLabel(group.groupType)}}</view>
				</view>
				<view class="item u-flex">
					<view class="name">活动时间</view>
					<view>{{group.activityTime}}</view>
				</view>
				<view class="item u-flex">
					<view class="name">活动地址</view>
					<view>{{group.address}}</view>
				</view>
			</view>
			<view class="tabs">
				<u-tabs active-color="#FFF533" inactive-color="#FFF" bg-color="#150C24" font-size="30" :list="list"
					:is-scroll="false" :current="current" @change="change"></u-tabs>
				<view class="ac-info u-flex" v-if="current===0">
					<view class="item u-flex">
						<view class="name">昵称</view>
						<view>{{myUserInfo.nickname}}</view>
					</view>
					<view class="item u-flex">
						<view class="name">性别</view>
						<view>{{myUserInfo.sex?'男':'女'}}</view>
					</view>
					<view class="item u-flex">
						<view class="name">星座</view>
						<view>{{returnStr(myUserInfo.constellation)}}</view>
					</view>
					<view class="item u-flex">
						<view class="name">年龄</view>
						<view>{{returnStr(myUserInfo.age)}}岁</view>
					</view>
					<view class="item u-flex">
						<view class="name">地区</view>
						<view>{{returnStr(myUserInfo.address)}}</view>
					</view>
					<view class="item u-flex">
						<view class="name">职业</view>
						<view>{{returnStr(myUserInfo.occupation)}}</view>
					</view>
					<view class="item u-flex">
						<view class="name">学校</view>
						<view>{{returnStr(myUserInfo.school)}}</view>
					</view>
				</view>
				<view class="tag" v-else>
					<cell-item :arrow="false" title="个人介绍">
						<text>
							{{myUserInfo.intro?myUserInfo.intro:'-'}}
						</text>
					</cell-item>
					<cell-item :arrow="false" title="兴趣爱好">
						<text>
							{{myUserInfo.hobby?myUserInfo.hobby:'-'}}
						</text>
					</cell-item>
					<cell-item :arrow="false" title="MBTI人格">
						<text>
							{{myUserInfo.mbti?myUserInfo.mbti:'-'}}
						</text>
					</cell-item>
					<cell-item :arrow="false" title="喜欢的音乐风格">
						<text>
							{{myUserInfo.musicStyle?myUserInfo.musicStyle:'-'}}
						</text>
					</cell-item>
				</view>
			</view>
			<view class="btns u-flex" v-if="type==='apply'">
				<button open-type="share" class="share" @click="share">分享</button>
				<button class="apply" @click="toApplyPage">申请加入</button>
			</view>
			<view class="btns trans u-flex" v-if="type==='update'">
				<view class="transfer u-flex">
					<image src="../../static/transfer.png" mode=""></image>
					转发
				</view>
				<button class="cancel" @click="cancel">取消</button>
				<button class="update" @click="toAcInfoUpdatePage(activityId,groupId)">修改</button>
			</view>
			<button class="agree" v-if="type === 'applied'" @click="agreenApply(applyId)">同意</button>
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
	import Api from "@/api/index.js"
	import utils from "@/utils/index.js"
	import cellItem from "@/components/cell-item/cell-item.vue"
	import {
		mapState
	} from "vuex"
	export default {
		components: {
			NavBar,
			cellItem
		},
		data() {
			return {
				list: [{
					name: '个人资料'
				}, {
					name: '个性标签'
				}],
				current: 0,
				myUserInfo: null,
				group: null,
				type: "",
				currentImage: "",
				currentIndex: 0
			}
		},
		computed: {
			...mapState(['userInfo'])
		},
		methods: {
			toActivityPage(activityId){
				uni.navigateTo({
					url: `/pages/activityDetail/index?activityId=${activityId}`
				})
			},
			setCurrentImage(val) {
				this.currentImage = val
			},
			// 同意申请
			agreenApply(applyId) {
				Api.agreeApply(applyId, (res) => {
					uni.navigateBack({
						delta: 1
					})
					uni.showToast({
						title: '组队成功'
					})
				})
			},
			share() {
				uni.share({
					provider: 'weixin',
					scene: "WXSceneSession",
					href: `/pages/info/index?activityId=${this.activityId}&groupId=${groupId}&type=${type}`,
					imageUrl: this.myUserInfo.avatar,
					title: this.myUserInfo.nickname
				})
			},
			change(index) {
				this.current = index;
			},
			// 前往申请页面
			toApplyPage() {
				if (this.userId === this.userInfo.userId) {
					uni.showToast({
						title: '不能申请自己的活动',
						icon: 'none'
					})
					return
				}
				uni.navigateTo({
					url: `/pages/info/apply/index?activityId=${this.activityId}&groupId=${this.groupId}`
				})
			},
			// 获取组队详情
			getActivityGroupDetail(groupId) {
				Api.getActivityGroupDetail(groupId, (res) => {
					const data = res.data.data
					this.group = data
					this.myUserInfo = data.user
					this.userId = data.userId
					this.currentImage = data.user.avatar
				})
			},
			// groupType
			returnGroupTypeLabel(val) {
				return utils.returnGroupTypeLabel(val)
			},
			returnStr(val) {
				return utils.returnStr(val)
			},
			// 取消按钮
			cancel() {
				uni.navigateBack({
					delta: 1
				})
			},
			// 前往活动信息修改页面
			toAcInfoUpdatePage(activityId, groupId) {
				uni.navigateTo({
					url: `/pages/party/update/index?activityId=${activityId}&groupId=${groupId}`
				})
			},
		},
		onLoad(l) {
			const {
				activityId,
				groupId,
				type,
				applyId
			} = l
			this.groupId = groupId
			this.activityId = activityId
			this.type = type
			this.applyId = applyId
			this.getActivityGroupDetail(groupId)
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>