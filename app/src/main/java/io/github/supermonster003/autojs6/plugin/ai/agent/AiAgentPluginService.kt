package io.github.supermonster003.autojs6.plugin.ai.agent

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Host-facing control plane of the plugin (`org.autojs.plugin.AI_AGENT`, category `ai-agent`),
 * living in the `:agent` process (roadmap D15).
 *
 * The real `IAiAgentPlugin` implementation (getInfo / getCapabilities / attach) arrives with
 * roadmap P2.5 once the host contract module `ai-agent-api` is staged in `libs/`. Until then
 * the service hands out a placeholder Binder that carries only the contract descriptor, so the
 * host can already discover the service, bind it and verify the descriptor without any
 * transaction being available.
 */
class AiAgentPluginService : Service() {

    private val binder: IBinder = Binder().apply {
        attachInterface(null, AiAgentPlugin.SERVICE_DESCRIPTOR)
    }

    override fun onBind(intent: Intent?): IBinder = binder
}
