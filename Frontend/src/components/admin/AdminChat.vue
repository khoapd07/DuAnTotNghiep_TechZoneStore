<template>
  <div class="container-fluid py-4">
    <div class="row h-100" style="min-height: 80vh;">
      
      <div class="col-md-4 border-end bg-white rounded-start shadow-sm p-0 d-flex flex-column">
        <div class="p-3 bg-light border-bottom">
          <h5 class="mb-0 fw-bold">Quản lý Chat AI</h5>
        </div>
        <div class="list-group list-group-flush overflow-auto" style="height: 70vh;">
          <button v-for="session in sessions" :key="session.sessionId" 
                  @click="selectSession(session)"
                  class="list-group-item list-group-item-action p-3"
                  :class="{ 'active bg-primary-green text-dark border-0': selectedSession?.sessionId === session.sessionId }">
            <div class="d-flex justify-content-between align-items-center mb-1">
              <strong class="text-truncate" style="max-width: 150px;">
                <i class="bi bi-person-circle me-1"></i> {{ formatCustomerName(session.sessionId) }}
              </strong>
              <small :class="selectedSession?.sessionId === session.sessionId ? 'text-dark' : 'text-muted'">
                {{ new Date(session.updatedAt).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) }}
              </small>
            </div>
            <div class="d-flex justify-content-between align-items-center mt-2">
              <span class="badge" :class="session.status === 'AI_HANDLING' ? 'bg-primary' : 'bg-warning text-dark'">
                {{ session.status === 'AI_HANDLING' ? '🤖 AI đang tiếp' : '👨‍💻 Bạn đang tiếp' }}
              </span>
            </div>
          </button>
        </div>
      </div>

      <div class="col-md-8 bg-white rounded-end shadow-sm p-0 d-flex flex-column">
        
        <div v-if="!selectedSession" class="d-flex flex-column align-items-center justify-content-center h-100 text-muted">
          <i class="bi bi-chat-dots fs-1 mb-2"></i>
          <h5>Chọn một khách hàng để hỗ trợ</h5>
        </div>

        <div v-else class="d-flex flex-column h-100">
          
          <div class="p-3 border-bottom d-flex justify-content-between align-items-center bg-light">
            <div>
              <h6 class="mb-0 fw-bold text-uppercase">{{ formatCustomerName(selectedSession.sessionId) }}</h6>
              <small class="text-muted">Trạng thái: {{ selectedSession.status === 'AI_HANDLING' ? 'Bot tự động' : 'Nhân viên thủ công' }}</small>
            </div>
            
            <div class="d-flex gap-2">
              <button v-if="selectedSession.status === 'AI_HANDLING'" @click="changeStatus('STAFF_HANDLING')" class="btn btn-warning btn-sm fw-bold shadow-sm">
                <i class="bi bi-person-raised-hand me-1"></i> Giành quyền
              </button>
              <button v-else @click="changeStatus('AI_HANDLING')" class="btn btn-primary btn-sm fw-bold shadow-sm">
                <i class="bi bi-robot me-1"></i> Giao lại cho Bot
              </button>

              <button @click="deleteChat" class="btn btn-danger btn-sm fw-bold shadow-sm" title="Xóa lịch sử chat này">
                <i class="bi bi-trash3"></i>
              </button>
            </div>
          </div>

          <div ref="chatContainer" class="flex-grow-1 p-3 overflow-auto" style="height: 50vh; background-color: #f8f9fa;">
            <div v-for="msg in messages" :key="msg.messageId" class="mb-3 d-flex flex-column">
              
              <div v-if="msg.senderType === 'CUSTOMER'" class="align-self-start text-dark bg-white border shadow-sm p-2 px-3 rounded-4 rounded-start-0" style="max-width: 75%;">
                {{ msg.content }}
              </div>

              <div v-else-if="msg.senderType === 'AI'" class="align-self-end text-white bg-primary shadow-sm p-2 px-3 rounded-4 rounded-end-0 mt-1" style="max-width: 75%;">
                <small class="d-block text-white-50" style="font-size: 0.7rem;">🤖 Bot</small>
                {{ msg.content }}
              </div>

              <div v-else-if="msg.senderType === 'STAFF'" class="align-self-end text-dark bg-primary-green shadow-sm p-2 px-3 rounded-4 rounded-end-0 mt-1" style="max-width: 75%;">
                <small class="d-block text-black-50" style="font-size: 0.7rem;">👨‍💻 Bạn</small>
                {{ msg.content }}
              </div>
            </div>
          </div>

          <div class="p-3 border-top bg-white">
            <div class="input-group">
              <input v-model="replyText" 
                     @keyup.enter="sendReply"
                     :disabled="selectedSession.status === 'AI_HANDLING'"
                     type="text" 
                     class="form-control" 
                     :placeholder="selectedSession.status === 'AI_HANDLING' ? 'Bạn phải bấm Giành quyền mới được nhắn tin...' : 'Nhập tin nhắn hỗ trợ khách...'">
              <button @click="sendReply" 
                      :disabled="selectedSession.status === 'AI_HANDLING' || !replyText.trim()"
                      class="btn btn-primary-green px-4 fw-bold">
                Gửi
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
// Xóa import axios từ thư viện gốc nếu có (file cũ dùng fetch), thay bằng api của bạn
import api from '../../utils/axios';

const sessions = ref([]);
const selectedSession = ref(null);
const messages = ref([]);
const replyText = ref('');
const chatContainer = ref(null);
let pollingInterval = null;

// Hàm làm đẹp tên khách hàng
const formatCustomerName = (sessionId) => {
  if (!sessionId) return 'Khách vô danh';
  if (sessionId.startsWith('sess_')) return 'Khách_' + sessionId.substring(5, 10);
  return sessionId; 
};

// Hàm lấy danh sách phiên chat
const fetchSessions = async () => {
  try {
    // Đổi fetch thành api.get
    const res = await api.get('/chat/sessions');
    sessions.value = res.data;
  } catch (error) { console.error(error); }
};

// Chọn 1 khách hàng để chat
const selectSession = async (session) => {
  selectedSession.value = session;
  messages.value = []; 
  await loadMessages(session.sessionId);
};

// Tải lịch sử tin nhắn của 1 phiên chat
const loadMessages = async (sessionId) => {
  try {
    // Đổi fetch thành api.get
    const res = await api.get(`/chat/session/${sessionId}/messages`);
    const data = res.data;
    // FIX LỖI CUỘN TRANG: Chỉ cập nhật nếu có tin nhắn mới
    if (messages.value.length < data.length) {
      messages.value = data;
      scrollToBottom();
    }
  } catch (error) { console.error(error); }
};

// Đổi trạng thái (AI_HANDLING <-> STAFF_HANDLING)
const changeStatus = async (newStatus) => {
  try {
    // Đổi fetch thành api.put
    await api.put(`/chat/session/${selectedSession.value.sessionId}/status`, { status: newStatus });
    selectedSession.value.status = newStatus;
    fetchSessions();
  } catch (error) { console.error(error); }
};

// ==========================================
// THÊM MỚI: HÀM XÓA TOÀN BỘ CHAT CỦA KHÁCH
// ==========================================
const deleteChat = async () => {
  if (!confirm('Bạn có chắc chắn muốn xóa toàn bộ lịch sử chat của khách hàng này không? Dữ liệu sẽ không thể khôi phục!')) return;
  
  try {
    // Đổi fetch thành api.delete
    await api.delete(`/chat/session/${selectedSession.value.sessionId}`);
    
    // Xóa thành công thì reset lại màn hình bên phải
    selectedSession.value = null; 
    messages.value = [];
    // Tải lại danh sách khách hàng bên trái
    fetchSessions(); 
    alert('Đã xóa thành công!');
  } catch (error) { 
    console.error('Lỗi xóa chat:', error); 
    alert('Có lỗi xảy ra khi xóa chat.');
  }
};

// Hàm gửi tin nhắn (Nhân viên)
const sendReply = async () => {
  if (!replyText.value.trim()) return;
  const content = replyText.value.trim();
  replyText.value = ''; 

  messages.value.push({ senderType: 'STAFF', content: content });
  scrollToBottom();

  try {
    // Đổi fetch thành api.post
    await api.post('/chat/message', {
      sessionId: selectedSession.value.sessionId,
      senderType: 'STAFF',
      content: content
    });
  } catch (error) { console.error(error); }
};

const scrollToBottom = () => {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
};

onMounted(() => {
  fetchSessions();
  pollingInterval = setInterval(() => {
    fetchSessions();
    if (selectedSession.value) {
      loadMessages(selectedSession.value.sessionId);
    }
  }, 3000);
});

onUnmounted(() => {
  if (pollingInterval) clearInterval(pollingInterval);
});
</script>

<style scoped>
.bg-primary-green { background-color: #3cf91a !important; }
.btn-primary-green { background-color: #3cf91a; color: #000; }
.btn-primary-green:hover { background-color: #34d616; }
</style>