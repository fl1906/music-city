import {
	mapState,
	mapActions
} from "vuex"
import Api from "@/api/index.js"
const mixin = {
	computed: {
		...mapState(['moreTimeShow', 'officialApplyId']),
	},
	data() {
		return {}
	},
	methods: {
		...mapActions(['closeMoreTime']),
		confirmToMoreTime(val) {
			Api.postWxzApply({
				applyId: this.officialApplyId,
				wxz: val
			}, (res) => {})
			this.closeMoreTime()
		},
	}
}

export default mixin