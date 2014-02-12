//
//  VOFactory.cpp
//  ErayOMGDemo
//
//  Created by Eray on 7/4/13.
//
//

#include "VOFactory.h"
using namespace ERAY::OMG;
<#list FieldList as field>
#include "${field.classNameStr}VO.h"
</#list>
VOFactory::VOFactory(){
    
}
VOFactory::~VOFactory(){
    
}

ERAY::OMG::ErayBaseVO* VOFactory::create(std::string voKey){
    ErayBaseVO* vo = NULL;
	<#list FieldList as field>
		if(voKey=="${field.classNameStr}"){
			vo = new ${field.classNameStr}VO();
			return vo;
		}
	</#list>
    return vo;
}
