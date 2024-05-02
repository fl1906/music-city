<template>
	<cover-view class="mask" :class="{hide:!show}" @click="cancel">
		<cover-view :class="{popup:true,hide:!show}" @click.stop="stopProp">
			<cover-view class="title">提示</cover-view>
			<cover-view :class="{content:true,show:mode=='moreTime'}">
				<cover-view>对方想要超限制确认</cover-view>
				<cover-view>已与本人见面</cover-view>
			</cover-view>
			<cover-view :class="{content:true,show:mode=='whenShow'}">
				<cover-view>·保障金将支付给对方</cover-view>
				<cover-view>·已超出免责取消，将收取0.1个派币</cover-view>
			</cover-view>
			<cover-view :class="{content:true,show:mode=='cancel'}">
				<cover-view>确认后将不可免责取消</cover-view>
			</cover-view>
			<cover-view class="btns u-flex">
				<button @click="cancel" class="cancel">{{cancelText}}</button>
				<button @click="confirm" class="confirm">确定</button>
			</cover-view>
		</cover-view>
	</cover-view>
</template>

<script>
	export default {
		props: {
			show: Boolean,
			cancelText: {
				type: String,
				default: "取消"
			},
			mode: {
				type: String,
				default: 'moreTime' // moreTime whenShow cancel
			}
		},
		methods: {
			stopProp(e) {},
			cancel() {
				this.$emit('close')
			},
			confirm() {
				this.$emit('confirm')
			}
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>