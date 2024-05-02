<template>
	<view class="apply">
		<NavBar :isBack="false" title="申请" />
		<Card title="活动信息">
			<view class="info">
				<view class="msg">
					<view class="u-flex">
						<image src="../../../static/msg-icon-yellow.png" class="msg-icon-yellow"></image>
						留言
					</view>
					<input v-model="message" />
				</view>
				<FundAndbudget @getFund="getFund" @getBudget="getBudget"></FundAndbudget>
			</view>
		</Card>
		<button class="submit" @click="submit">确定申请</button>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Card from "@/components/card/card.vue"
	import FundAndbudget from "@/components/fundAndbudget/fundAndbudget.vue"
	import Api from "@/api/index.js"
	import utils from "@/utils/index.js"
	export default {
		components: {
			NavBar,
			Card,
			FundAndbudget
		},
		data() {
			return {
				message: '',
			}
		},
		methods: {
			getBudget(val) {
				this.money = val.value
			},
			getFund(val) {
				this.groupType = val
			},
			submit() {
				const data = {
					activityId: this.activityId,
					groupId: this.groupId,
					groupType: this.groupType,
					money: this.money,
					message: this.message
				}
				utils.fieldEffect(data.message, '留言')
				utils.fieldEffect(data.groupType, '保障金')
				utils.fieldEffect(data.money, '经费')
				Api.postTeamApply(data, (res) => {
					uni.navigateBack({
						delta: 2
					})
					uni.showToast({
						title: '申请成功'
					})
				})
			}
		},
		onLoad(l) {
			const {
				activityId,
				groupId
			} = l
			this.groupId = groupId
			this.activityId = activityId
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>