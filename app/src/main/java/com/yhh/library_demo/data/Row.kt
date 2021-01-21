package com.yhh.library_demo.data


import com.google.gson.annotations.SerializedName

data class Row(
    @SerializedName("ADRES")
    val aDRES: String,
    @SerializedName("CHARGER_EMAIL")
    val cHARGEREMAIL: String,
    @SerializedName("CODE_VALUE")
    val cODEVALUE: String,
    @SerializedName("FDRM_CLOSE_DATE")
    val fDRMCLOSEDATE: String,
    @SerializedName("FLOOR_DC")
    val fLOORDC: String,
    @SerializedName("FOND_YEAR")
    val fONDYEAR: String,
    @SerializedName("FXNUM")
    val fXNUM: String,
    @SerializedName("GU_CODE")
    val gUCODE: String,
    @SerializedName("HMPG_URL")
    val hMPGURL: String,
    @SerializedName("LBRRY_INTRCN")
    val lBRRYINTRCN: String,
    @SerializedName("LBRRY_NAME")
    val lBRRYNAME: String,
    @SerializedName("LBRRY_SE_NAME")
    val lBRRYSENAME: String,
    @SerializedName("LBRRY_SEQ_NO")
    val lBRRYSEQNO: String,
    @SerializedName("LON_GDCC")
    val lONGDCC: String,
    @SerializedName("MBER_SBSCRB_RQISIT")
    val mBERSBSCRBRQISIT: String,
    @SerializedName("OP_TIME")
    val oPTIME: String,
    @SerializedName("TEL_NO")
    val tELNO: String,
    @SerializedName("TFCMN")
    val tFCMN: String,
    @SerializedName("XCNTS")
    val xCNTS: String,
    @SerializedName("YDNTS")
    val yDNTS: String
)