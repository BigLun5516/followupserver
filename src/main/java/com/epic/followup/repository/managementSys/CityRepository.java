package com.epic.followup.repository.managementSys;
import com.epic.followup.model.managementSys.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<CityModel, Integer>{
    @Query(nativeQuery =true, value ="SELECT city_name,university_num FROM management_city "+
            "order by university_num desc")
    java.util.List<Object> getCityModelOrderByCover();
}
