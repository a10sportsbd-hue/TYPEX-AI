package com.typex.ai.ai

interface AiProvider {
    suspend fun process(request: AiRequest): AiResult
}
