package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.EventEntity
import com.katar.app.data.EventViewModel
import com.katar.app.data.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    eventViewModel: EventViewModel,
    memberViewModel: MemberViewModel
) {
    val akanDatang by eventViewModel.akanDatang.collectAsState()
    val selesai by eventViewModel.selesai.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEvent by remember { mutableStateOf<EventEntity?>(null) }
    var deletingEvent by remember { mutableStateOf<EventEntity?>(null) }
    var detailEventId by remember { mutableStateOf<Int?>(null) }

    // Kalau sedang di detail screen
    if (detailEventId != null) {
        AgendaDetailScreen(
            eventId = detailEventId!!,
            eventViewModel = eventViewModel,
            memberViewModel = memberViewModel,
            onBack = { detailEventId = null }
        )
        return
    }

    val currentList = if (selectedTab == 0) akanDatang else selesai

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Agenda Karang Taruna",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "${akanDatang.size} akan datang · ${selesai.size} selesai",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Kegiatan")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab row
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Akan Datang (${akanDatang.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Riwayat (${selesai.size})") }
                )
            }

            // Daftar kegiatan
            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (selectedTab == 0) "📅" else "✅",
                            fontSize = 64.sp
                        )
                        Text(
                            text = if (selectedTab == 0) "Belum ada agenda"
                                   else "Belum ada riwayat",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Klik tombol + untuk menambah",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentList, key = { it.id }) { event ->
                        EventCard(
                            event = event,
                            onAbsensiClick = { detailEventId = it.id },
                            onEditClick = { editingEvent = it },
                            onDeleteClick = { deletingEvent = it }
                        )
                    }
                }
            }
        }
    }

    // Dialog tambah
    if (showAddDialog) {
        EventFormDialog(
            onDismiss = { showAddDialog = false },
            onSave = {
                eventViewModel.addEvent(it)
                showAddDialog = false
            }
        )
    }

    // Dialog edit
    editingEvent?.let { ev ->
        EventFormDialog(
            event = ev,
            onDismiss = { editingEvent = null },
            onSave = {
                eventViewModel.updateEvent(it)
                editingEvent = null
            }
        )
    }

    // Dialog konfirmasi hapus
    deletingEvent?.let { ev ->
        AlertDialog(
            onDismissRequest = { deletingEvent = null },
            title = { Text("Hapus Kegiatan?") },
            text = {
                Text("Yakin ingin menghapus \"${ev.nama_kegiatan}\"? Semua data absensi juga akan terhapus.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        eventViewModel.deleteEvent(ev)
                        deletingEvent = null
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingEvent = null }) {
                    Text("Batal")
                }
            }
        )
    }
}