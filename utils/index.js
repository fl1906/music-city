const returnStr = (val) => {
	if (val === null || val === undefined) return "-"
	return val
}

const returnGroupTypeLabel = (val) => {
	const GroupTypeList = [{
			value: 0,
			label: 'AA制'
		},
		{
			value: 1,
			label: '我买单'
		}, {
			value: 2,
			label: '对方买单'
		}
	]
	return GroupTypeList[val].label
}

const isNull = (val) => {
	return (!val || val.length == 0)
}

// 校验字段
const fieldEffect = (val, mess) => {
	if (val == 0) return
	if (isNull(val)) {
		uni.showToast({
			title: mess + "不能为空",
			icon: "error"
		})
		throw mess + "不能为空"
	}
}

export default {
	returnStr,
	returnGroupTypeLabel,
	isNull,
	fieldEffect
}