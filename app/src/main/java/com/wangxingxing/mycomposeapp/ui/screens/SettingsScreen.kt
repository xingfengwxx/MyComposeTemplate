package com.wangxingxing.mycomposeapp.ui.screens

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.* 
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hjq.permissions.permission.PermissionLists
import com.wangxingxing.mycomposeapp.utils.PermissionUtils
import com.wangxingxing.mycomposeapp.utils.getActivity
import com.wangxingxing.mycomposeapp.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    // 在 Composable 顶层获取 Activity
    val activity = getActivity()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Divider()
                
                SettingsItem(
                    title = "Notifications",
                    description = "Manage notification preferences"
                )
                
                Divider()
                
                SettingsItem(
                    title = "测试按钮抖动",
                    description = "测试按钮抖动"
                ) {
                    viewModel.testSingleClick()
                }
                
                Divider()
                
                SettingsItem(
                    title = "About",
                    description = "App version and information"
                )

                Divider()

                // 使用新的权限请求方式（不依赖 AspectJ）
                SettingsItem(
                    title = "请求文件读写权限",
                    description = "请求文件读写权限"
                ) {
                    // 使用在顶层获取的 activity
                    PermissionUtils.requestPermissions(
                        permissions = arrayOf(
                            PermissionLists.getReadMediaImagesPermission(),
                            PermissionLists.getReadMediaVideoPermission(),
                            PermissionLists.getReadMediaAudioPermission(),PermissionLists.getReadMediaVisualUserSelectedPermission()
                        ),
                        onGranted = {
                            // 权限授予后执行 ViewModel 方法
                            viewModel.requestFilePermissions()
                        },
                        onDenied = {
                            // 权限被拒绝的处理（可选）
                        }
                    )
                }
            }
        }
    }
}

/**
 * 设置项的可组合组件。
 * @param title 标题
 * @param description 描述
 * @param onClick (可选) 点击事件回调
 */
@Composable
fun SettingsItem(
    title: String,
    description: String,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick) // 如果 onClick 不为 null, 则使其可点击
    } else {
        Modifier
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
