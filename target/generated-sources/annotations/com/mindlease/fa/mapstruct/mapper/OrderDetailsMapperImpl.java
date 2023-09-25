package com.mindlease.fa.mapstruct.mapper;

import com.mindlease.fa.dto.OrderDetailsDto;
import com.mindlease.fa.model.OrderDetails;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-25T16:10:51+0530",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 20.0.2 (Oracle Corporation)"
)
@Component
public class OrderDetailsMapperImpl implements OrderDetailsMapper {

    @Override
    public OrderDetailsDto convertOrderDetailsToOrderDetailsDto(OrderDetails orderDetails) {
        if ( orderDetails == null ) {
            return null;
        }

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();

        orderDetailsDto.setId( orderDetails.getId() );
        orderDetailsDto.setDbs_fa_date( orderDetails.getDbs_fa_date() );
        orderDetailsDto.setDbs_ag_name( orderDetails.getDbs_ag_name() );
        orderDetailsDto.setDbs_material( orderDetails.getDbs_material() );
        orderDetailsDto.setDbs_lotid( orderDetails.getDbs_lotid() );
        orderDetailsDto.setDbs_wfr( orderDetails.getDbs_wfr() );
        orderDetailsDto.setDbs_part( orderDetails.getDbs_part() );
        orderDetailsDto.setDbs_prio( orderDetails.getDbs_prio() );
        orderDetailsDto.setDbs_location( orderDetails.getDbs_location() );
        orderDetailsDto.setDbs_step( orderDetails.getDbs_step() );
        orderDetailsDto.setDbs_status( orderDetails.getDbs_status() );
        orderDetailsDto.setDbs_elee( orderDetails.getDbs_elee() );
        orderDetailsDto.setDbs_famo( orderDetails.getDbs_famo() );
        orderDetailsDto.setDbs_car( orderDetails.getDbs_car() );
        orderDetailsDto.setDbs_fa_reason( orderDetails.getDbs_fa_reason() );
        orderDetailsDto.setDbs_fa_descr( orderDetails.getDbs_fa_descr() );
        orderDetailsDto.setDbs_link( orderDetails.getDbs_link() );
        orderDetailsDto.setDbs_method( orderDetails.getDbs_method() );
        orderDetailsDto.setDbs_pos_xcmap( orderDetails.isDbs_pos_xcmap() );
        orderDetailsDto.setDbs_pos_text( orderDetails.getDbs_pos_text() );
        orderDetailsDto.setDbs_pos_01( orderDetails.isDbs_pos_01() );
        orderDetailsDto.setDbs_pos_02( orderDetails.isDbs_pos_02() );
        orderDetailsDto.setDbs_pos_03( orderDetails.isDbs_pos_03() );
        orderDetailsDto.setDbs_pos_04( orderDetails.isDbs_pos_04() );
        orderDetailsDto.setDbs_pos_05( orderDetails.isDbs_pos_05() );
        orderDetailsDto.setDbs_pos_06( orderDetails.isDbs_pos_06() );
        orderDetailsDto.setDbs_pos_07( orderDetails.isDbs_pos_07() );
        orderDetailsDto.setDbs_pos_08( orderDetails.isDbs_pos_08() );
        orderDetailsDto.setDbs_pos_09( orderDetails.isDbs_pos_09() );
        orderDetailsDto.setDbs_pos_10( orderDetails.isDbs_pos_10() );
        orderDetailsDto.setDbs_pos_11( orderDetails.isDbs_pos_11() );
        orderDetailsDto.setDbs_pos_12( orderDetails.isDbs_pos_12() );
        orderDetailsDto.setDbs_pos_13( orderDetails.isDbs_pos_13() );
        orderDetailsDto.setDbs_pos_14( orderDetails.isDbs_pos_14() );
        orderDetailsDto.setDbs_pos_15( orderDetails.isDbs_pos_15() );
        orderDetailsDto.setDbs_pos_16( orderDetails.isDbs_pos_16() );
        orderDetailsDto.setDbs_pos_17( orderDetails.isDbs_pos_17() );
        orderDetailsDto.setDbs_pos_18( orderDetails.isDbs_pos_18() );
        orderDetailsDto.setDbs_pos_19( orderDetails.isDbs_pos_19() );
        orderDetailsDto.setDbs_pos_20( orderDetails.isDbs_pos_20() );
        orderDetailsDto.setDbs_pos_21( orderDetails.isDbs_pos_21() );
        orderDetailsDto.setDbs_remain( orderDetails.getDbs_remain() );
        orderDetailsDto.setDbs_fa_name( orderDetails.getDbs_fa_name() );
        orderDetailsDto.setDbs_fa_text( orderDetails.getDbs_fa_text() );
        orderDetailsDto.setDbs_fa_start( orderDetails.getDbs_fa_start() );
        orderDetailsDto.setDbs_fa_stop( orderDetails.getDbs_fa_stop() );
        orderDetailsDto.setDbs_fa_archiv_wf( orderDetails.getDbs_fa_archiv_wf() );
        orderDetailsDto.setDbs_fa_archiv_ps( orderDetails.getDbs_fa_archiv_ps() );
        orderDetailsDto.setDbs_res_name( orderDetails.getDbs_res_name() );
        orderDetailsDto.setDbs_res_text( orderDetails.getDbs_res_text() );
        orderDetailsDto.setDbs_res_start( orderDetails.getDbs_res_start() );
        orderDetailsDto.setDbs_res_stop( orderDetails.getDbs_res_stop() );
        orderDetailsDto.setDbs_cost( orderDetails.getDbs_cost() );
        orderDetailsDto.setDbs_wait_time1( orderDetails.getDbs_wait_time1() );
        orderDetailsDto.setDbs_fa_time( orderDetails.getDbs_fa_time() );
        orderDetailsDto.setDbs_wait_time2( orderDetails.getDbs_wait_time2() );
        orderDetailsDto.setDbs_res_time( orderDetails.getDbs_res_time() );
        orderDetailsDto.setDbs_cpl_time( orderDetails.getDbs_cpl_time() );
        orderDetailsDto.setCurrentTab( orderDetails.getCurrentTab() );
        orderDetailsDto.setPreviousTab( orderDetails.getPreviousTab() );
        orderDetailsDto.setNextTab( orderDetails.getNextTab() );
        orderDetailsDto.setTab( orderDetails.getTab() );
        orderDetailsDto.setAction( orderDetails.getAction() );
        orderDetailsDto.setLn( orderDetails.getLn() );
        orderDetailsDto.setPart( orderDetails.getPart() );
        orderDetailsDto.setDbs_ag_name_temp( orderDetails.getDbs_ag_name_temp() );
        orderDetailsDto.setDbs_prio_temp( orderDetails.getDbs_prio_temp() );
        orderDetailsDto.setDbs_material_temp( orderDetails.getDbs_material_temp() );
        orderDetailsDto.setDbs_lotid_temp( orderDetails.getDbs_lotid_temp() );
        orderDetailsDto.setDbs_part_temp( orderDetails.getDbs_part_temp() );
        orderDetailsDto.setDbs_fa_reason_temp( orderDetails.getDbs_fa_reason_temp() );
        orderDetailsDto.setDbs_elee_temp( orderDetails.getDbs_elee_temp() );
        orderDetailsDto.setDbs_famo_temp( orderDetails.getDbs_famo_temp() );
        orderDetailsDto.setDbs_location_temp( orderDetails.getDbs_location_temp() );
        orderDetailsDto.setDbs_status_temp( orderDetails.getDbs_status_temp() );
        orderDetailsDto.setDbs_method_temp( orderDetails.getDbs_method_temp() );

        return orderDetailsDto;
    }
}
