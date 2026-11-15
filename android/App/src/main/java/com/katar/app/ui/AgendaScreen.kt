package com.katar.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Align
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxIntrinsics
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.HorizontalPager
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Chip
import androidx.compose.material3.ChipGroup
import androidx.compose.material3.Dialog
import androidx.compose.material3.DialogPane
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ImagePicker
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Disposal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.graphics.vector.vectorPainter
import androidx.compose.ui.input.keyboard.keyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalRetrofit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.textUnit
import androidx.compose.ui.unit.size
import androidx.compose.ui.unit.width
import com.katar.app.R
import com.katar.app.data.EventEntity
import com.katar.app.data.EventViewModel
import com.katar.app.data.MemberEntity
import com.katar.app.data.EventEntityWithCount
import com.katar.app.navigation.route
import com.katar.app.navigation.routeString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.run.catcher

/**
 * AgendaScreen - Compose UI for Agenda & Presensi Karang Taruna.
 * Memisahkan tab 'Akan Datang' dan 'Riwayat Kegiatan', serta halaman detail kegiatan
 * yang memiliki checklist absensi cepat dari daftar MemberEntity yang sudah ada.
 */
@Composable
fun AgendaScreen(viewModel: EventViewModel = EventViewModel(EventRepository(EventDao(), MemberDao())) ) {

    // Collect state
    var akanDatangList by remember { mutableStateOf<List<EventEntity>>() }
    var selesaiList by remember { mutableStateOf<List<EventEntity>>() }
    var eventsWithAttendance by remember { mutableStateOf<List<EventEntityWithCount>>() }

    viewModel.akanDatang.collect { list ->
        akanDatangList = list
    }
    viewModel.selesai.collect { list ->
        selesaiList = list
    }
    viewModel.eventsWithAttendance.collect { list ->
        eventsWithAttendance = list
    }

    // Tab state
    var tabIndex by remember { mutableStateOf(0) } // 0 = Akan Datang, 1 = Riwayat
    var showFilter by remember { mutableStateOf(false) }

    // Collect members for checklist
    var allMembers by remember { mutableStateOf<List<MemberEntity>>() }
    // In full implementation, collect from memberRepository

    // Screen content
    Scaffold(
        bottomBar = {
            // Bottom Navigation for Agenda module
            BottomNavigationBarAgenda(tabIndex = { tabIndex = it },
                onSelect = { tabIndex = it })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Top
        ) {
            // --- HEADER WITH ADD EVENT BUTTON ---
            Row(
                modifier = Modifier.fillMaxSize().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (tabIndex) {
                        0 -> "Agenda Akan Datang"
                        1 -> "Riwayat Kegiatan"
                        else -> "Agenda"
                    },
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.width(8.dp))

                // FAB sederhana untuk tambah event
                OutlinedButton(
                    onClick = {
                        // Toggle ke form add event
                        showFilter = !showFilter
                    }
                ) {
                    Text(text = "Tambah Kegiatan")
                }
            }

            // --- TAB CONTENT ---
            when (tabIndex) {
                0 -> {
                    // TAB 1: Akan Datang
                    VerticalPager(
                        modifier = Modifier.fillMaxSize(),
                        index = 0,
                        onIndexChanged = { index -> tabIndex = index }
                    ) { page ->
                        // Daftar event akan datang
                        if (akanDatangList.isEmpty()) {
                            Center(
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = "Belum ada agenda yang akan datang",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            akanDatangList.forEach { event ->
                                EventCard(
                                    event = event,
                                    onDetail = {
                                        // Navigasi ke detail event
                                        // viewModel.detailEvent(event.id) 
                                    },
                                    onToggleAttendance = {
                                        // Toggle kehadiran
                                        // viewModel.toggleAttendance(event.id, memberId, hadir)
                                    }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 2: Riwayat Kegiatan
                    VerticalPager(
                        modifier = Modifier.fillMaxSize(),
                        index = 1,
                        onIndexChanged = { index -> tabIndex = index }
                    ) { page ->
                        if (selesaiList.isEmpty()) {
                            Center(
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = "Belum ada riwayat kegiatan",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            selesaiList.forEach { event ->
                                EventCard(
                                    event = event,
                                    onDetail = {
                                        // Navigasi ke detail event
                                    },
                                    onToggleAttendance = {
                                        // Toggle kehadiran
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** VerticalPager - Pager vertikal untuk tab alternatif */
@Composable
fun VerticalPager(
    modifier: Modifier = Modifier,
    index: Int,
    onIndexChanged: (Int) -> Unit,
    content: @Composable (Int) -> Unit
) {
    // Implementasi sederhana menggunakan TabRow + Page
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedIndex = index,
        onSelectChanged = onIndexChanged,
        indicator = { tab ->
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        content(0)
        Tab(
            text = { Text("Akan Datang") },
            icon = {
                Icon(
                    imageVector = androidx.compose.material3.Icons.Filled.Calendar,
                    contentDescription = "Akan Datang"
                )
            }
        )
        Tab(
            text = { Text("Riwayat") },
            icon = {
                Icon(
                    imageVector = androidx.compose.material3.Icons.Filled.History,
                    contentDescription = "Riwayat"
                )
            }
        )
    }

    content(0) // Content akan diganti sesuai need
}

/** EventCard - Card menampilkan detail kegiatan dengan checklist absensi */
@Composable
fun EventCard(
    event: EventEntity,
    onDetail: () -> Unit,
    onToggleAttendance: (memberId: Int, hadir: Boolean) -> Unit
) {
    val isAkanDatang = event.status == "AKAN_DATANG"
    val warnaStatus = if (isAkanDatang) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    val warnaTeks = if (isAkanDatang) {
        MaterialTheme.colorScheme.onTertiary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(shape = androidx.compose.ui.shape.RoundedCornerShape(12.dp)),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header event
            Row(
                modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge
                Chip(
                    text = { Text(text = event.status, overflow = androidx.compose.ui.textOverflow.Ellipsis) },
                    backgroundColor = warnaStatus,
                    color = warnaTeks
                )

                Spacer(Modifier.width(8.dp))

                // Info kegiatan
                Column(
                    modifier = Modifier.fillMaxWeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = event.nama_kegiatan,
                        style = MaterialTheme.typography.body1,
                        overflow = androidx.compose.ui.textOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${event.tanggal_waktu?.substring(0, 10) ?: "-"} | ${event.lokasi ?: "-"}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Action button detail
                IconButton(
                    onClick = onDetail,
                    contentDescription = "Detail kegiatan ${event.nama_kegiatan}"
                ) {
                    ImageVector(
                        painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_info),
                        contentDescription = "Detail"
                    )
                }
            }

            // Detail info
            Text(
                text = event.deskripsi ?: "Tidak ada deskripsi",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = androidx.compose.ui.textOverflow.Elipsis
            )

            // Checklist absensi
            Text(
                text = "Daftar Kehadiran",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            // Checklist members - mock data untuk sekarang
            // Di implementasi full: collect members dari database dan checkbox kehadiran
            if (event.status == "AKAN_DATANG") {
                // Daftar anggota dengan checkbox absensi
                // Mock: 5 anggota contoh
                MemberChecklist(
                    eventId = event.id,
                    members = generateMockMembers(),
                    onAttendanceChanged = onToggleAttendance
                )
            }
        }
    }
}

/** MemberChecklist - Checklist kehadiran anggota */
@Composable
fun MemberChecklist(
    eventId: Int,
    members: List<MemberEntity>,
    onAttendanceChanged: (memberId: Int, hadir: Boolean) -> Unit
) {
    if (members.isEmpty()) {
        Text(
            text = "Tidak ada anggota terdaftar",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.Center)
        )
    } else {
        members.forEach { member ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar/Initials
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            if (member.jabatan?.startsWith("Ket") or member.jabatan?.startsWith("Ketua")) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                ) {
                    Text(
                        text = member.nama.isNotEmpty() member.nama.substring(0, 1).uppercase() else "?",
                        style = MaterialTheme.typography.body1.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Member info
                Column(
                    modifier = Modifier.fillMaxWeight().padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = member.nama,
                        style = MaterialTheme.typography.body2,
                        overflow = androidx.compose.ui.textOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = member.jabatan ?: "Anggota",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Checkbox kehadiran
                var checked by remember { mutableStateOf(false) }

                // Cek status kehadiran dari database
                // viewModel.getMemberAttendance(eventId, member.id) { status ->
                //     checked = status
                // }

                // Untuk sekarang pakai checkbox sederhana
                // Di implementasi full: gunakan ToggleChip dengan state dari ViewModel
                Chip(
                    text = { Text(text = "Hadir") },
                    selected = checked,
                    onClick = {
                        checked = !checked
                        onAttendanceChanged(member.id, checked)
                    }
                )
            }
        }
    }
}

/** Generate mock members for checklist */
private fun generateMockMembers(): List<MemberEntity> {
    return listOf(
        MemberEntity(
            id = 1,
            nama = "Budi Santoso",
            no_anggota = "KA-001",
            jabatan = "Ketua",
            rt = "01",
            rw = "03",
            no_hp = "081234567890",
            status_aktif = true
        ),
        MemberEntity(
            id = 2,
            nama = "Siti Rahmawati",
            no_anggota = "KA-002",
            jabatan = "Sekretaris",
            rt = "02",
            rw = "04",
            no_hp = "081987654321",
            status_aktif = true
        ),
        MemberEntity(
            id = 3,
            nama = "Andy Putra",
            no_anggota = "KA-003",
            jabatan = "Anggota",
            rt = "03",
            rw = "05",
            no_hp = "085112233445",
            status_aktif = true
        ),
        MemberEntity(
            id = 4,
            nama = "Dewi Lestari",
            no_anggota = "KA-004",
            jabatan = "",
            rt = "04",
            rw = "06",
            no_hp = "087777777777",
            status_aktif = true
        ),
        MemberEntity(
            id = 5,
            nama = "Raka Optimusa",
            no_anggota = "KA-005",
            jabatan = "",
            rt = "05",
            rw = "07",
            no_hp = "088888888888",
            status_aktif = false
        )
    )
}