<template>
	<view class="update">
		<NavBar :isBack="false" title="修改信息" />
		<Card title="活动信息">
			<view class="info">
				<AcTitle @getTitle="getTitle"></AcTitle>
				<FundAndbudget @getFund="getFund" @getBudget="getBudget"></FundAndbudget>
			</view>
		</Card>
		<AcTime @getTime="getTime" />
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
		<button class="submit u-flex" @click="submit">保存</button>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Card from "@/components/card/card.vue"
	import AcTitle from "@/components/ac-title/ac-title.vue"
	import AcTime from "@/components/ac-time/ac-time.vue"
	import FundAndbudget from "@/components/fundAndbudget/fundAndbudget.vue"
	import Api from '@/api/index.js'
	import dayjs from "../../../common/dayjs/day.js"

	export default {
		components: {
			NavBar,
			Card,
			AcTitle,
			AcTime,
			FundAndbudget
		},
		methods: {
			getFund(val) {
				this.fund = val
				console.log(val)
			},
			getTitle(val) {
				this.title = val
			},
			getBudget(val) {
				this.budget = val
			},
			getTime(val) {
				this.time = val
			},
			submit() {
				const {
					title,
					fund,
					time,
					budget,
					address,
					activityId,
					groupId,
					group,
				} = this;
				console.log(group)
				const data = {
					...group,
					groupId,
					activityId,
					title,
					money: fund,
					activityTime: time,
					groupType: budget.value,
					updateTime: dayjs().format("YYYY-MM-DD HH:mm:ss")
				}
				Api.postTeamUpInfo(data, (res) => {
					uni.navigateBack({
						delta: 1
					})
				})
			},
			// 获取组队详情
			getActivityGroupDetail(groupId) {
				Api.getActivityGroupDetail(groupId, (res) => {
					console.log(res.data)
					this.group = res.data.data
				})
			}
		},
		onLoad(l) {
			const {
				activityId,
				groupId
			} = l;
			this.groupId = groupId
			this.activityId = activityId
			this.getActivityGroupDetail(groupId)
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>