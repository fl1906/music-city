<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发起方" prop="fromUserId">
        <el-input
          v-model="queryParams.fromUserId"
          placeholder="请输入发起方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接受方" prop="toUserId">
        <el-input
          v-model="queryParams.toUserId"
          placeholder="请输入接受方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息" prop="message">
        <el-input
          v-model="queryParams.message"
          placeholder="请输入消息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息状态" prop="messageStatus">
        <el-select v-model="queryParams.messageStatus" placeholder="请选择消息状态" clearable>
          <el-option
            v-for="dict in dict.type.user_talk_msg_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="createTime">
        <el-date-picker clearable
          v-model="queryParams.createTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="" prop="updateTime">
        <el-date-picker clearable
          v-model="queryParams.updateTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:userTalk:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:userTalk:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:userTalk:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:userTalk:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userTalkList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="聊天ID" align="center" prop="talkId" v-if="true"/>
      <el-table-column label="发起方" align="center" prop="fromUserId" />
      <el-table-column label="接受方" align="center" prop="toUserId" />
      <el-table-column label="消息" align="center" prop="message">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_talk_msg_type" :value="scope.row.message"/>
        </template>
      </el-table-column>
      <el-table-column label="消息状态" align="center" prop="messageStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_talk_msg_status" :value="scope.row.messageStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="消息类型" align="center" prop="messageType" />
      <el-table-column label="" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:userTalk:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:userTalk:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户聊天对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发起方" prop="fromUserId">
          <el-input v-model="form.fromUserId" placeholder="请输入发起方" />
        </el-form-item>
        <el-form-item label="接受方" prop="toUserId">
          <el-input v-model="form.toUserId" placeholder="请输入接受方" />
        </el-form-item>
        <el-form-item label="消息" prop="message">
          <el-input v-model="form.message" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="消息状态" prop="messageStatus">
          <el-radio-group v-model="form.messageStatus">
            <el-radio
              v-for="dict in dict.type.user_talk_msg_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUserTalk, getUserTalk, delUserTalk, addUserTalk, updateUserTalk } from "@/api/system/userTalk";

export default {
  name: "UserTalk",
  dicts: ['user_talk_msg_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户聊天表格数据
      userTalkList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fromUserId: undefined,
        toUserId: undefined,
        message: undefined,
        messageStatus: undefined,
        messageType: undefined,
        createTime: undefined,
        updateTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        talkId: [
          { required: true, message: "聊天ID不能为空", trigger: "blur" }
        ],
        fromUserId: [
          { required: true, message: "发起方不能为空", trigger: "blur" }
        ],
        toUserId: [
          { required: true, message: "接受方不能为空", trigger: "blur" }
        ],
        message: [
          { required: true, message: "消息不能为空", trigger: "blur" }
        ],
        messageStatus: [
          { required: true, message: "消息状态不能为空", trigger: "change" }
        ],
        messageType: [
          { required: true, message: "消息类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户聊天列表 */
    getList() {
      this.loading = true;
      listUserTalk(this.queryParams).then(response => {
        this.userTalkList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        talkId: undefined,
        fromUserId: undefined,
        toUserId: undefined,
        message: undefined,
        messageStatus: undefined,
        messageType: undefined,
        createTime: undefined,
        updateTime: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.talkId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户聊天";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const talkId = row.talkId || this.ids
      getUserTalk(talkId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改用户聊天";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.talkId != null) {
            updateUserTalk(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserTalk(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const talkIds = row.talkId || this.ids;
      this.$modal.confirm('是否确认删除用户聊天编号为"' + talkIds + '"的数据项？').then(() => {
        this.loading = true;
        return delUserTalk(talkIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/userTalk/export', {
        ...this.queryParams
      }, `userTalk_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
