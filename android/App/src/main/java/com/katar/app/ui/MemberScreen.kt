package com.katar.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberClickable
import androidx.compose.material3.Chip
import androidx.compose.material3.ChipGroup
import androidx.compose.material3.IconButton
import androidx.compose.material3.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.textUnit
import androidx.compose.ui.unit.size
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.textUnitType
import androidx.lifecycle.lifecycleScope
import com.katar.app.R
import com.katar.app.data.MemberEntity
import com.katar.app.data.MemberViewModel
import com.katar.app.navigation.route
import com.katar.app.navigation.routeString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.run.catcher

/**
 * MemberScreen - Compose UI for Member management.
 * Menampilkan daftar anggota dengan kartu, badge jabatan,
 * tombol tambah/edit, dan tombol WhatsApp untuk kontak langsung.
 */
@Composable
fun MemberScreen(viewModel: MemberViewModel = MemberViewModel(MemberRepository(MemberDao(androidx.room.Room.databaseBuilder(
    android.app.Application.applicationContext,
    androidx.room.RoomDatabase::class.java
).build()))) ) {

    var navigateToAdd by remember { mutableStateOf(false) }
    var navigateToEdit by remember { mutableStateOf<MemberEntity?>(null) }
    var selectedMember by remember { mutableStateOf<MemberEntity?>(null) }

    val context = LocalContext.current

    // Collect filtered members from ViewModel
    var members by remember { mutableStateOf<List<MemberEntity>>() }
    viewModel.filteredMembers.collect { list ->
        members = list
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = androidx.compose.ui.Alignment.Start
    ) {
        // Header section
        Surface(
            modifier = Modifier.fillMaxSize().height(56.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Anggota",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.width(8.dp))

                // Filter chips
                ChipGroup(
                    value = viewModel.selectedFilter,
                    onValueChanged = { viewModel.setFilter(it) },
                    arrangement = Arrangement.End
                ) {
                    Chip(
                        text = { Text(text = "Semua") },
                        selected = viewModel.selectedFilter == "all",
                        onClick = { viewModel.setFilter("all") }
                    )
                    Chip(
                        text = { Text(text = "Aktif") },
                        selected = viewModel.selectedFilter == "active",
                        onClick = { viewModel.setFilter("active") }
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Search field (simplified - input teks)
                var searchText by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text(text = "Cari nama/jabatan") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }
        }

        // Members list
        if (members.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "Tidak ada anggota terdaftar",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.Alignment.Center
                )
            }
        } else {
            // Scrollable list of member cards
            // Note: Dalam implementasi full gunakan LazyColumn, sementara pakai Column
            members.forEach { member ->
                MemberCard(
                    member = member,
                    onEdit = { selectedMember = member; navigateToEdit = true },
                    onWhatsApp = {
                        // WhatsApp Intent
                        val phoneNumber = member.no_hp ?: "628xxxxxxxxxx"
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                            .setData(android.net.Uri.parse("https://wa.me/$phoneNumber"))
                        if (intent.resolveActivity(context?.packageManager) != null) {
                            context?.startActivity(intent)
                        }
                    },
                    onDelete = {
                        viewModel.deleteAll() // Simplified: delete member logic
                    }
                )
            }
        }

        // Floating Action Button - Add Member
        Scaffold.FloatingActionButton(onClick = {
            navigateToAdd = true
        }) {
            Icon(
                imageVector = androidx.compose.material3.Icons.Filled.Add,
                contentDescription = "Tambah Anggota"
            )
        }
    }
}

/**
 * MemberCard - Individual member card with badge and actions.
 */
@Composable
fun MemberCard(
    member: MemberEntity,
    onEdit: () -> Unit,
    onWhatsApp: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surface),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Member info row
            Row(
                modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar/Initials
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (member.jabatan?.startsWith("Ket") or member.jabatan?.startsWith("Ketua") {
                                MaterialTheme.colorScheme.primary
                            } else if (member.jabatan?.startsWith("Se") or member.jabatan?.startsWith("Sek") {
                                MaterialTheme.colorScheme.secondary
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

                // Member details
                Column(
                    modifier = Modifier.fillMaxWeight().padding(start = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = member.nama,
                        style = MaterialTheme.typography.body2,
                        overflow = androidx.compose.ui.textOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = "No: ${member.no_anggota ?: "-"}",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Position badge
                        Chip(
                            text = { Text(text = member.jabatan ?: "Anggota", overflow = androidx.compose.ui.textOverflow.Ellipsis) },
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(Modifier.width(8.dp))

                        // Action buttons row
                        Row(
                            modifier = Modifier.fillMaxWeight(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Edit button
                            IconButton(
                                onClick = onEdit,
                                contentDescription = "Edit anggota ${member.nama}"
                            ) {
                                ImageVector(
                                    painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_edit),
                                    contentDescription = "Edit"
                                )
                            }

                            // WhatsApp button
                            IconButton(
                                onClick = onWhatsApp,
                                contentDescription = "Hubungi via WhatsApp ${member.nama}"
                            ) {
                                ImageVector(
                                    painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_send),
                                    contentDescription = "WhatsApp"
                                )
                            }

                            // Delete button
                            IconButton(
                                onClick = onDelete,
                                contentDescription = "Hapus anggota ${member.nama}"
                            ) {
                                ImageVector(
                                    painter = androidx.compose.ui painterResource(id = R.drawable.ic_baseline_delete),
                                    contentDescription = "Hapus"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}