<template>
	<view>
		<view class="acd">
			<NavBar :isBack="false" title="活动详情" />
			<u-loading :show="showLoading"></u-loading>
			<template v-if="!showLoading">
				<image class="cover" :src="detail.innerImage"></image>
				<view class="info">
					<view class="title">{{detail.title}}</view>
					<view class="sponsor">主办方</view>
					<view class="split-line">
						<image src="../../static/split-line.png"></image>
					</view>
				</view>
				<view class="wh u-flex">
					<view class="time u-flex">
						<image src="../../static/time-icon.png" mode=""></image>
						<text class="time-text">
							{{getMonth(detail.startTime)}} - {{getMonth(detail.endDate)}}
							{{getPmOrAm(detail.endDate)}}
						</text>
					</view>
					<image src="../../static/split-line-vertical.png" class="split-line-vertical"></image>
					<view class="position u-flex">
						<image src="../../static/position-icon.png"></image>
						<text class="position-text">{{detail.address}}</text>
					</view>
				</view>
				<view class="line"></view>
				<view class="cate">
					<view class="cate-title">音乐风格</view>
					<view class="cate-title-en">music style</view>
					<view class="content music u-flex">
						<view class="item u-flex" v-for="item in detail.tagList" :key="item.tagId">
							{{item.name}}
						</view>
					</view>
				</view>
				<view class="cate">
					<view class="cate-title">艺人阵容</view>
					<view class="cate-title-en">Artist lineup</view>
					<view class="content artist u-flex">
						<view v-for="(item,index) in detail.artistList" :key="item.artistId" class="item u-flex"
							@click="setArtist(item,index)">
							<image :src="item.imageUrl">
							</image>
							<view>
								{{item.name}}
							</view>
						</view>
					</view>
				</view>
				<view class="cate">
					<view class="cate-title">场地舞台</view>
					<view class="cate-title-en">Venue stage</view>
					<view class="content venue u-flex">
						<view class="item u-flex" v-for="item in detail.stageList" :key="item.introId">
							<image class="cover" :src="item.imageFullUrl">
							</image>
							<view>
								{{item.content}}
							</view>
						</view>
					</view>
				</view>
				<view class="cate">
					<view class="cate-title">更多介绍</view>
					<view class="cate-title-en">More introduction</view>
					<view v-for="(item,index) in detail.introList" :key="index">
						<view class="content more">
							{{currentShowMore === index ?item.content.slice(100):item.content}}
							<text v-if="currentShowMore===index">...</text>
						</view>
						<view class="more-content" v-if="currentShowMore===index" @click="setMore">
							查看更多内容
						</view>
						<image :src="item.imageFullUrl" class="intro-more"></image>
					</view>
				</view>
				<view class="cate">
					<view class="cate-title">购票通道</view>
					<view class="cate-title-en">Ticket systems</view>
					<view class="content ticket">
						<view>
							<image src="../../static/ticket-bg.png" class="ticket-bg"></image>
							<view class="info u-flex">
								<view class="desc">订座</view>
								<view class="count u-flex">
									{{detail.organizerList.phone}}
									<image src="../../static/copy-icon-yellow.png"
										@click="copy(detail.organizerList.phone)"></image>
								</view>
							</view>
						</view>
						<view class="channel u-flex">
							<view class="item u-flex" v-for="item in detail.organizerList.organizerTickets"
								:key="item.organizerTicketId">
								<image src="../../static/qrcode-border.png" class="qrcode-border"></image>
								<image :src="item.qrImage" class="qrcode"></image>
								<image :src="item.logoImage" class="logo"></image>
							</view>
						</view>
					</view>
				</view>
			</template>
		</view>
		<view class="fix-bottom u-flex">
			<image src="../../static/activity-detail-bg.png" class="activity-detail-bg"></image>
			<view class="nav" @click="handleNav">
				<image src="../../static/love-icon.png" v-if="!navStatus"></image>
				<image src="../../static/nav-red.png" v-else></image>
				<view>收藏</view>
			</view>
			<button open-type="share" class="share" @click="share">分享</button>
			<button class="team-up" @click="toTeamUpPage(activityId)">去组队</button>
		</view>
		<u-popup mode="center" v-model="showArtist" :closeable="false">
			<view class="artist-content">
				<view class="title">{{artistTitle}}</view>
				<view>
					{{artist}}
				</view>
			</view>
		</u-popup>
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
	import dayjs from '@/common/dayjs/day.js'

	export default {
		components: {
			NavBar
		},
		data() {
			return {
				detail: {},
				activityId: null,
				navStatus: false,
				currentShowMore: -1,
				showLoading: false,
				showArtist: false,
				artist: "",
				artistTitle: ""
			}
		},
		computed: {},
		methods: {
			setArtist(item) {
				this.showArtist = true
				this.artist = item.description
				this.artistTitle = item.name
			},
			hideArtist() {
				this.showArtist = false
			},
			share() {
				uni.share({
					provider: 'weixin',
					scene: "WXSceneSession",
					href: `/pages/activityDetail/index?activityId=${this.activityId}`,
					imageUrl: this.detail.coverImage,
					title: this.detail.title
				})
			},
			setMore() {
				this.showMore = !this.showMore
			},
			copy(value) {
				uni.setClipboardData({
					data: value, //要被复制的内容
					success: () => { //复制成功的回调函数
						uni.showToast({ //提示
							title: '复制成功'
						})
					}
				})
			},
			// 前往组队页面
			toTeamUpPage(activityId) {
				uni.navigateTo({
					url: '/pages/teamUp/index?activityId=' + activityId
				})
			},
			// 获取活动详情
			getActivityDetail(id) {
				this.showLoading = true
				Api.getActivityDetail(id, (res) => {
					const data = res.data.data
					this.detail = data
					this.showMore = data.introList[0].content.length > 100
					this.showLoading = false
				})
			},
			getMonth(time) {
				if (!time) return;
				return dayjs(time).format('MM.DD')
			},
			getPmOrAm(time) {
				if (!time) return
				return (dayjs(time).hour() > 12 ? "PM. " : "AM. ") + dayjs(time).format('HH:mm')
			},
			// 收藏操作
			handleNav() {
				Api.postNavActivity({
					activityId: this.activityId
				}, (res) => {
					this.getNavActivity(this.activityId)
				})
			},
			// 查询收藏状态
			getNavActivity(activityId) {
				Api.getNavActivityAndList({
					activityId,
					time: new Date().getTime()
				}, (res) => {
					const data = res.data.rows
					this.navStatus = data.length !== 0
				})
			}
		},
		onLoad(l) {
			const {
				activityId
			} = l
			this.activityId = activityId
			this.getActivityDetail(activityId)
			this.getNavActivity(activityId)
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>