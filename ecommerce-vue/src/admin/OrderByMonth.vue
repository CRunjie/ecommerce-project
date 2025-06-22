<template>
    <div class="demo-date-picker">
        <div class="block">
            <el-form :inline="true" :model="searchParam">
				<el-form-item label="日期范围">
                    <el-date-picker
                            v-model="searchParam.orderdate"
                            value-format="YYYY-MM"
                            type="monthrange"
                            range-separator="To"
                            start-placeholder="Start month"
                            end-placeholder="End month"
                        />
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="onSubmit()">查询</el-button>
				</el-form-item>
			</el-form>
        </div>
    </div>
	<div id="myChart" :style="{width: '100%', height: '380px'}"></div>
</template>
<script setup>
import { onMounted } from 'vue'
import * as echarts from 'echarts';
import { ElMessage } from 'element-plus'
import { reactive} from 'vue'
import axios from '../axios/request.js'
let searchParam = reactive({})
const onSubmit = () => {//查询
    const datev = searchParam.orderdate
    let start1 = '';
    let end1 = '';
    let data11 = []
    let data22 = []
    if(datev != null){
        start1 = datev[0]
        end1 = datev[1]
    }
    axios.myPost('/api/goods/selectOrderByMonth',
    {
        startDate: start1,
        endDate: end1
    })
    .then(res => {
        for (let i = 0; i < res.data.resData.length; i++){
            data11.push(res.data.resData[i].months)
            data22.push(res.data.resData[i].totalamount)
        }
        mydraw(data11, data22)
    })
    .catch((error) => {
        ElMessage.error(error)
    })
}
const mydraw = (datav1, datav2) => {
    const myChart = echarts.init(document.getElementById('myChart'))
    const option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: datav1,
            axisTick: {
                alignWithLabel: true
            }
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
            data: datav2,
            type: 'bar',
            name: '销量（元）',
            }
        ]
    }
    myChart.setOption(option)
}
onMounted (() => {
    onSubmit()
})
</script>
<style scoped>
.demo-date-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
}
.demo-date-picker .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  flex: 1;
}
</style>
