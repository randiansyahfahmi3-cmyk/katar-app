package com.katar.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katar.app.data.EventViewModel
import com.katar.app.data.MemberEntity
import com.katar.app.data.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaDetailScreen(
    eventId: Int,
    eventViewModel: EventViewModel,
    memberViewModel: MemberViewModel,
    onBack: () -> Unit
) {
    val members by memberViewModel.members.collectAsState()
    val allAttendance by eventViewModel.getAttendanceByEvent(eventId).collectAsState(initial = emptyList())

    // State lokal: map<memberId, hadir>
    val attendanceMap = remember { mutableStateMapOf<Int, Boolean>() }

    // Sync dari database → local state
    LaunchedEffect(allAttendance) {
        attendanceMap.clear()
        allAttendance.forEach { att ->
            attendanceMap[att.member_id] = att.hadir
        }
    }

    // Ambil event dari list (simple lookup)
    val events by eventViewModel.events.collectAsState()
    val event = events.find { it.id == eventId }

    val totalHadir = attendanceMap.values.count { it }
    val totalAnggota = members.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            event?.nama_kegiatan ?: "Detail Kegiatan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            "$totalHadir / $totalAnggota hadir",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Info event
            if (event != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (!event.lokasi.isNullOrBlank()) {
                            Text(
                                text = "📍 ${event.lokasi}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (!event.deskripsi.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.deskripsi,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Progress bar kehadiran
            if (totalAnggota > 0) {
                LinearProgressIndicator(
                    progress = { totalHadir.toFloat() / totalAnggota.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Daftar anggota + checkbox
            if (members.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "👥", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Belum ada anggota",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Tambahkan anggota dulu di tab Anggota",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(members, key = { it.id }) { member ->
                        AttendanceRow(
                            member = member,
                            hadir = attendanceMap[member.id] ?: false,
                            onToggle = { checked ->
                                attendanceMap[member.id] = checked
                                eventViewModel.toggleAttendance(
                                    eventId = eventId,
                                    memberId = member.id,
                                    hadir = checked
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceRow(
    member: MemberEntity,
    hadir: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (hadir) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = hadir,
                onCheckedChange = onToggle
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.nama,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (!member.jabatan.isNullOrBlank()) {
                    Text(
                        text = member.jabatan,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}