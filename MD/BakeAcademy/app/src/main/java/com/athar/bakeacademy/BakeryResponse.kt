package com.athar.bakeacademy

import com.google.gson.annotations.SerializedName

data class BakeryResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("nutrisi")
	val nutrisi: Nutrisi,

	@field:SerializedName("trsBahan")
	val trsBahan: String
)

data class FOLAC(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class ParsedItem(

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("measure")
	val measure: String,

	@field:SerializedName("retainedWeight")
	val retainedWeight: Float,

	@field:SerializedName("foodId")
	val foodId: String,

	@field:SerializedName("weight")
	val weight: Float,

	@field:SerializedName("foodMatch")
	val foodMatch: String,

	@field:SerializedName("food")
	val food: String,

	@field:SerializedName("measureURI")
	val measureURI: String,

	@field:SerializedName("nutrients")
	val nutrients: Nutrients,

	@field:SerializedName("status")
	val status: String
)

data class CHOLE(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class NIA(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class CHOCDFNet(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class VITK1(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class Data(

	@field:SerializedName("langkah")
	val langkah: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("bahan")
	val bahan: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("nutrisi")
	val nutrisi: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("deskripsi")
	val deskripsi: String
)

data class FATRN(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class TotalDaily(

	@field:SerializedName("VITB6A")
	val vITB6A: VITB6A,

	@field:SerializedName("VITC")
	val vITC: VITC,

	@field:SerializedName("CHOCDF")
	val cHOCDF: CHOCDF,

	@field:SerializedName("K")
	val k: K,

	@field:SerializedName("VITD")
	val vITD: VITD,

	@field:SerializedName("P")
	val p: P,

	@field:SerializedName("CHOLE")
	val cHOLE: CHOLE,

	@field:SerializedName("ENERC_KCAL")
	val eNERCKCAL: ENERCKCAL,

	@field:SerializedName("FASAT")
	val fASAT: FASAT,

	@field:SerializedName("VITK1")
	val vITK1: VITK1,

	@field:SerializedName("MG")
	val mG: MG,

	@field:SerializedName("RIBF")
	val rIBF: RIBF,

	@field:SerializedName("CA")
	val cA: CA,

	@field:SerializedName("NIA")
	val nIA: NIA,

	@field:SerializedName("THIA")
	val tHIA: THIA,

	@field:SerializedName("FIBTG")
	val fIBTG: FIBTG,

	@field:SerializedName("VITB12")
	val vITB12: VITB12,

	@field:SerializedName("TOCPHA")
	val tOCPHA: TOCPHA,

	@field:SerializedName("PROCNT")
	val pROCNT: PROCNT,

	@field:SerializedName("FOLDFE")
	val fOLDFE: FOLDFE,

	@field:SerializedName("NA")
	val nA: NA,

	@field:SerializedName("ZN")
	val zN: ZN,

	@field:SerializedName("VITA_RAE")
	val vITARAE: VITARAE,

	@field:SerializedName("FAT")
	val fAT: FAT,

	@field:SerializedName("FE")
	val fE: FE
)

data class TotalNutrients(

	@field:SerializedName("VITB6A")
	val vITB6A: VITB6A,

	@field:SerializedName("FAMS")
	val fAMS: FAMS,

	@field:SerializedName("VITC")
	val vITC: VITC,

	@field:SerializedName("CHOCDF")
	val cHOCDF: CHOCDF,

	@field:SerializedName("K")
	val k: K,

	@field:SerializedName("VITD")
	val vITD: VITD,

	@field:SerializedName("FATRN")
	val fATRN: FATRN,

	@field:SerializedName("P")
	val p: P,

	@field:SerializedName("CHOLE")
	val cHOLE: CHOLE,

	@field:SerializedName("ENERC_KCAL")
	val eNERCKCAL: ENERCKCAL,

	@field:SerializedName("FASAT")
	val fASAT: FASAT,

	@field:SerializedName("VITK1")
	val vITK1: VITK1,

	@field:SerializedName("CHOCDF.net")
	val cHOCDFNet: CHOCDFNet,

	@field:SerializedName("MG")
	val mG: MG,

	@field:SerializedName("SUGAR.added")
	val sUGARAdded: SUGARAdded,

	@field:SerializedName("RIBF")
	val rIBF: RIBF,

	@field:SerializedName("CA")
	val cA: CA,

	@field:SerializedName("FOLFD")
	val fOLFD: FOLFD,

	@field:SerializedName("WATER")
	val wATER: WATER,

	@field:SerializedName("FAPU")
	val fAPU: FAPU,

	@field:SerializedName("NIA")
	val nIA: NIA,

	@field:SerializedName("THIA")
	val tHIA: THIA,

	@field:SerializedName("FIBTG")
	val fIBTG: FIBTG,

	@field:SerializedName("VITB12")
	val vITB12: VITB12,

	@field:SerializedName("TOCPHA")
	val tOCPHA: TOCPHA,

	@field:SerializedName("SUGAR")
	val sUGAR: SUGAR,

	@field:SerializedName("PROCNT")
	val pROCNT: PROCNT,

	@field:SerializedName("FOLDFE")
	val fOLDFE: FOLDFE,

	@field:SerializedName("NA")
	val nA: NA,

	@field:SerializedName("ZN")
	val zN: ZN,

	@field:SerializedName("VITA_RAE")
	val vITARAE: VITARAE,

	@field:SerializedName("FAT")
	val fAT: FAT,

	@field:SerializedName("FOLAC")
	val fOLAC: FOLAC,

	@field:SerializedName("FE")
	val fE: FE
)

data class THIA(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class ZN(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class VITB6A(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FAT(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class K(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class PROCNT(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class SUGARAdded(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class SUGAR(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FIBTG(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FASAT(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FAMS(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class Nutrisi(

	@field:SerializedName("dietLabels")
	val dietLabels: List<String>,

	@field:SerializedName("cautions")
	val cautions: List<Any>,

	@field:SerializedName("healthLabels")
	val healthLabels: List<String>,

	@field:SerializedName("yield")
	val yield: Int,

	@field:SerializedName("totalWeight")
	val totalWeight: Any,

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>,

	@field:SerializedName("totalDaily")
	val totalDaily: TotalDaily,

	@field:SerializedName("calories")
	val calories: Int,

	@field:SerializedName("uri")
	val uri: String,

	@field:SerializedName("totalNutrients")
	val totalNutrients: TotalNutrients
)

data class VITC(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FE(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class WATER(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class VITB12(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class IngredientsItem(

	@field:SerializedName("parsed")
	val parsed: List<ParsedItem>,

	@field:SerializedName("text")
	val text: String
)

data class CA(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class ENERCKCAL(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class TOCPHA(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FOLDFE(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class MG(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class VITD(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class P(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class RIBF(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FOLFD(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class CHOCDF(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class Nutrients(

	@field:SerializedName("VITB6A")
	val vITB6A: VITB6A,

	@field:SerializedName("FAMS")
	val fAMS: FAMS,

	@field:SerializedName("VITC")
	val vITC: VITC,

	@field:SerializedName("VITD")
	val vITD: VITD,

	@field:SerializedName("CHOCDF")
	val cHOCDF: CHOCDF,

	@field:SerializedName("K")
	val k: K,

	@field:SerializedName("P")
	val p: P,

	@field:SerializedName("CHOLE")
	val cHOLE: CHOLE,

	@field:SerializedName("ENERC_KCAL")
	val eNERCKCAL: ENERCKCAL,

	@field:SerializedName("FASAT")
	val fASAT: FASAT,

	@field:SerializedName("VITK1")
	val vITK1: VITK1,

	@field:SerializedName("MG")
	val mG: MG,

	@field:SerializedName("RIBF")
	val rIBF: RIBF,

	@field:SerializedName("CA")
	val cA: CA,

	@field:SerializedName("FOLFD")
	val fOLFD: FOLFD,

	@field:SerializedName("WATER")
	val wATER: WATER,

	@field:SerializedName("FAPU")
	val fAPU: FAPU,

	@field:SerializedName("NIA")
	val nIA: NIA,

	@field:SerializedName("THIA")
	val tHIA: THIA,

	@field:SerializedName("TOCPHA")
	val tOCPHA: TOCPHA,

	@field:SerializedName("VITB12")
	val vITB12: VITB12,

	@field:SerializedName("FIBTG")
	val fIBTG: FIBTG,

	@field:SerializedName("SUGAR")
	val sUGAR: SUGAR,

	@field:SerializedName("PROCNT")
	val pROCNT: PROCNT,

	@field:SerializedName("FOLDFE")
	val fOLDFE: FOLDFE,

	@field:SerializedName("NA")
	val nA: NA,

	@field:SerializedName("VITA_RAE")
	val vITARAE: VITARAE,

	@field:SerializedName("ZN")
	val zN: ZN,

	@field:SerializedName("FAT")
	val fAT: FAT,

	@field:SerializedName("FOLAC")
	val fOLAC: FOLAC,

	@field:SerializedName("FE")
	val fE: FE,

	@field:SerializedName("SUGAR.added")
	val sUGARAdded: SUGARAdded,

	@field:SerializedName("FATRN")
	val fATRN: FATRN
)

data class VITARAE(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class FAPU(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)

data class NA(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("quantity")
	val quantity: Float,

	@field:SerializedName("label")
	val label: String
)
