<template>
	<view class="team-up">
		<NavBar :isBack="false" title="组队" />
		<Dropdown ref="drop" title="等级" :titleSize="30" height="450" class="drop">
			<view class="label u-flex">
				<text>1级</text>
				<text>10级</text>
			</view>
			<slider :value="value" max="10" min="1" @changing="sliderChange" />
			<view class="filter">
				{{value}}级{{value>1?"及以下":""}}
			</view>
			<view class="btns u-flex">
				<button class="cancel" @click="reset">重置</button>
				<button class="submit" @click="submit">确定</button>
			</view>
		</Dropdown>
		<Dropdown ref="dropFilter" title="筛选" :titleSize="30" height="450" class="fil">
			<view class="gender u-flex">
				<button :class="{active:gender==0}" @click="gender = 0">仅看男生</button>
				<button :class="{active:gender==1}" @click="gender = 1">仅看女生</button>
			</view>
			<view class="btns u-flex">
				<button class="cancel" @click="reset">重置</button>
				<button class="submit" @click="submit">确定</button>
			</view>
		</Dropdown>
		<view class="line"></view>
		<u-loading :show="showLoading" v-if="showLoading"></u-loading>
		<ScrollList @scrollBottom="scrollBottom" height="60vh" v-else>
			<TeamCard v-for="(item,index) in acGroupList" :key="index" :item="item" @click="toUserInfoPage"
				class="item" />
		</ScrollList>
		<view class="fix-bottom u-flex" @click="toCreateTeamUpPage(activityId)">
			创建组队
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
	import ScrollList from "@/components/scroll-list/scroll-list.vue"
	import Dropdown from "@/components/dropdown/dropdown.vue"
	import {
		mapState
	} from "vuex"
	export default {
		components: {
			NavBar,
			TeamCard,
			ScrollList,
			Dropdown
		},
		data() {
			return {
				pageNum: 1,
				acGroupList: [],
				value: null,
				showLoading: false,
				gender: null //0 男 1 女
			}
		},
		methods: {
			reset() {
				this.value = null
				this.gender = null
			},
			submit() {
				const gender = this.gender ? this.gender : ""
				const value = this.value ? this.value : ""
				this.getActivityGroupList(this.activityId, value, gender, () => {
					this.$refs.drop.closeOpen();
					this.$refs.dropFilter.closeOpen();
				})
			},
			sliderChange(e) {
				const value = e.detail.value
				this.value = value
			},
			// 前往创建组队页面
			toCreateTeamUpPage(activityId) {
				uni.navigateTo({
					url: "/pages/teamUp/create/index?activityId=" + activityId
				})
			},
			// 前往个人信息页面
			toUserInfoPage({
				groupId,
				activityId,
				userId
			}) {
				uni.navigateTo({
					url: `/pages/info/index?groupId=${groupId}&activityId=${activityId}&userId=${userId}&type=apply`
				})
			},
			getActivityGroupList(activityId, userLevelMax, userSex, success) {
				this.showLoading = true
				Api.getActivityGroupList({
					activityId,
					userLevelMax: userLevelMax ? userLevelMax : "",
					userSex: userSex ? userSex : ""
				}, (res) => {
					const data = res.data.rows
					this.acGroupList = data
					this.showLoading = false
					success()
				})
			},
			scrollBottom() {
				this.pageNum += 1
			}
		},
		onLoad(l) {
			const {
				activityId,
				userId,
				gender,
				value
			} = l
			this.activityId = activityId
			this.userId = userId
			this.getActivityGroupList(activityId, value, gender)
		},
		onShow() {
			const {
				activityId,
				gender,
				value
			} = this;
			if (activityId) {
				this.getActivityGroupList(activityId, value, gender)
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>