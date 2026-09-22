package io.github.supermonster003.autojs6.plugin.ai.agent.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import io.github.supermonster003.autojs6.plugin.ai.agent.AiAgentPlugin
import io.github.supermonster003.autojs6.plugin.ai.agent.R

/**
 * Standalone entry of the plugin (roadmap D2 / D11). The task workbench of roadmap P6 grows out
 * of this screen; the P0 preview only reports whether a compatible AutoJs6 host is installed.
 */
class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        findViewById<TextView>(R.id.launcher_host_status).text = hostStatusText()
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.launcher_host_status).text = hostStatusText()
    }

    private fun hostStatusText(): CharSequence {
        val snapshot = readHostPackage()
        val required = AiAgentPlugin.REQUIRED_HOST_VERSION
        return when (classifyHostPresence(snapshot, required)) {
            HostPresence.MISSING -> getString(R.string.launcher_host_missing, required)
            HostPresence.DISABLED -> getString(R.string.launcher_host_disabled)
            HostPresence.INCOMPATIBLE -> getString(R.string.launcher_host_incompatible, snapshot!!.versionCode, required)
            HostPresence.READY -> getString(R.string.launcher_host_ready, snapshot!!.versionName, snapshot.versionCode)
        }
    }

    private fun readHostPackage(): HostPackageSnapshot? {
        val packageInfo = try {
            packageManager.getPackageInfo(AiAgentPlugin.HOST_PACKAGE_NAME, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        return HostPackageSnapshot(
            enabled = packageInfo.applicationInfo?.enabled ?: false,
            versionCode = versionCode,
            versionName = packageInfo.versionName.orEmpty(),
        )
    }
}
