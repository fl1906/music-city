<template>

  <div class="app-container">
    <div>
      <h1>待办事项</h1>
      <el-result icon="success" :title="'代办事件条数为'+tableData.length.toString()"/>
    </div>
    <el-input v-model="input" placeholder="请输入需要填入的代办事项" @change="handleAdd(input)"></el-input>
    <hr>

    <el-table
      :data="tableData"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
      style="width: 100%">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        label="编号"
        width="180">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="事件名称"
        width="180">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>事件名称: {{ scope.row.name }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.name }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        label="完成状态"
        width="100">
        <template slot-scope="scope">
          <el-checkbox id="checkbox" v-model="scope.row.complete"></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-button type="danger" @click="deleteData" v-show="tableData.length">批量删除代办事项</el-button>
    <el-button type="danger" @click="deleteCompleteData" v-show="tableData.length">批量清除已完成代办事项</el-button>
  </div>


</template>


<script>
export default {
  data() {
    return {
      input: '',
      tableData: JSON.parse(localStorage.getItem("tableData")) || [],
      multipleSelection: []
    }
  },
  watch:{
    "tableData":{
      deep:true,
      handler(newData){
        localStorage.setItem("tableData",JSON.stringify(newData))
      }
    }
  },
  methods: {
    handleEdit(index, row) {
      this.$prompt('请输入新的代办事项', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: row.name
      }).then(({ value }) => {
        row.name=value
        this.$message({
          type: 'success',
          message: '修改后的代办事项是: ' + value
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    handleDelete(index,row) {
      console.log(row);
      this.tableData = this.tableData.filter(item => item.id !== row.id); //过滤掉选中的那个值并且 重新赋值给Data
    },
    handleAdd(data) {
      let lastItemId =0
      if(this.tableData.length>0)
      {
        lastItemId = this.tableData[this.tableData.length - 1].id;
      }
      // Create a new item object with input as name and last item's ID + 1
      const newItem = {
        id: lastItemId + 1,
        name: this.input,
        complete: false
      };
      console.log("当前用户输入的代办事项为: ", this.input);

      // Add the new item to the tableData array
      this.tableData.push(newItem);
      this.input=''
    }
    ,
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log("多选",this.multipleSelection)
    },
    batchDelete(){
      console.log("删除方法1 全局匹配")
      // console.log("233",this.multipleSelection.includes(2))
      this.tableData = this.tableData.filter(item => !this.multipleSelection.includes(item));
    },
    deleteData() {
      console.log("删除方法2 只匹配Id")
      this.tableData = this.tableData.filter(item => !this.multipleSelection.some(data => data.id === item.id));
    },
    deleteCompleteData()
    {
      console.log("删除已完成的代办事项")
      console.log("2333",JSON.parse(localStorage.getItem("tableData")).filter(item => !item.complete === true))
      localStorage.setItem("tableData",JSON.stringify(JSON.parse(localStorage.getItem("tableData")).filter(item => !item.complete === true)));
      this.tableData=JSON.parse(localStorage.getItem("tableData"))
    }
  },
  created() {

  }


}
</script>

<style scoped>
     #checkbox.el-checkbox__input.is-checked.el-checkbox__inner{
       background-color: greenyellow;
       border-color: greenyellow;

     }
</style>
