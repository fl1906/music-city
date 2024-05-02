<template>
	<u-cell-item :title="title" :arrow="arrow" :border-bottom="false" hover-class="none">
		<slot></slot>
	</u-cell-item>
</template>

<script>
	export default {
		name: "cell-item",
		props: {
			title: String,
			arrow: Boolean
		},
		data() {
			return {

			}
		}
	}
</script>

<style>

</style>