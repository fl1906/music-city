<template>
	<view class="level">
		<NavBar :isBack="false" title="等级" />
		<view class="user u-flex">
			<image class="avatar" :src="userInfo.avatarUrl">
			</image>
			<view class="info u-flex">
				<view class="title">{{userInfo.nickName}}</view>
				<view class="tip">恭喜您已达到该等级</view>
			</view>
		</view>
		<view class="level-bg">
			<image :src="levelInfo.lv1.bg" mode=""></image>
			<view class="point u-flex">
				<image :src="levelInfo.lv1.gou" class="lv1-gou"></image>
				<text class="range">0-30积分</text>
				<text class="line">/</text>
				<text class="current">当前积分25</text>
			</view>
			<view class="cancel u-flex">
				<view class="title u-flex">
					<image :src="levelInfo.lv1.left"></image>
					<text>每天一次无责取消</text>
					<image :src="levelInfo.lv1.right"></image>
				</view>
				<view class="tip">当前已达到Lv.1等级，每天一次无责取消</view>
			</view>
			<view class="counts u-flex">
				<view class="count u-flex">
					<image :src="levelInfo.lv1.pair" class="pair"></image>
					<text>匹配成功</text>
					<view class="count-str u-flex">
						+
						<text>3</text>
					</view>
				</view>
				<view class="count u-flex">
					<image :src="levelInfo.lv1.good" class="pair"></image>
					<text>获得好评</text>
					<view class="count-str u-flex">
						+
						<text>3</text>
					</view>
				</view>
				<view class="count u-flex">
					<image :src="levelInfo.lv1.bad" class="pair"></image>
					<text>获得差评</text>
					<view class="count-str u-flex">
						-
						<text>3</text>
					</view>
				</view>
			</view>
		</view>
		<view class="point-cate u-flex">
			<view class="lv u-flex active">
				<view class="title">Lv.1</view>
				<view class="pointer"></view>
			</view>
			<view class="lv u-flex">
				<view class="title">Lv.2</view>
				<view class="pointer"></view>
			</view>
			<view class="lv u-flex">
				<view class="title">Lv.3</view>
				<view class="pointer"></view>
			</view>
			<view class="lv u-flex">
				<view class="title">Lv.4</view>
				<view class="pointer"></view>
			</view>
			<view class="lv u-flex">
				<view class="title">Lv.5</view>
				<view class="pointer"></view>
			</view>
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import {
		mapState
	} from 'vuex' //引入mapState
	export default {
		components: {
			NavBar
		},
		computed: {
			...mapState({
				userInfo: state => state.userInfo,
			})
		},
		data() {
			return {
				levelInfo: {
					lv1: {
						bg: 'https://paizhicheng.oss-cn-beijing.aliyuncs.com/2023/07/20/76eae3373d604b30b14c82886f44cb1e.png',
						gou: '../../../static/lv1-gou.png',
						left: '../../../static/lv1tag-left.png',
						right: '../../../static/lv1tag-right.png',
						pair: '../../../static/pair.png',
						good: '../../../static/good.png',
						bad: '../../../static/bad.png'
					}
				}
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>