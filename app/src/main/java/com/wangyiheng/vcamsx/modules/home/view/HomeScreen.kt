import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.wangyiheng.vcamsx.modules.home.controllers.HomeController
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


@Composable
fun HomeScreen() {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val homeController = remember { HomeController() }
    LaunchedEffect(Unit){
        homeController.init()
    }

    val selectVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        videoUri = uri
        uri?.let { homeController.copyVideoToAppDir(context,it) }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                selectVideoLauncher.launch("video/*")
            } else {
                // Handle permission denial
                Toast.makeText(context, "请打开设置允许读取文件夹权限", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(onClick = {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }) {
                Text("选择视频")
            }

            Row {
                Text("视频开关：")
                Switch(
                    checked = homeController.isVideoEnabled.value,
                    onCheckedChange = {
                        homeController.isVideoEnabled.value = it
                        homeController.saveState()
                        Toast.makeText(context, (if (it) "视频打开" else "视频关闭"), Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Row {
                Text("音量开关：")
                Switch(
                    checked = homeController.isVolumeEnabled.value,
                    onCheckedChange = {
                        homeController.isVolumeEnabled.value = it
                        homeController.saveState()
                        Toast.makeText(context, (if (it) "声音打开" else "声音关闭"), Toast.LENGTH_SHORT).show()
                    }
                )
            }

        }
    }
}